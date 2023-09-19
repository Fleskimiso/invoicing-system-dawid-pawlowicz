package pl.futurecollars.invoicing.db.sql;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.annotation.Transactional;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Car;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;
import pl.futurecollars.invoicing.model.Vat;

class InvoiceSqlDatabase extends AbstractSqlDatabase implements Database<Invoice> {

  public InvoiceSqlDatabase(JdbcTemplate jdbcTemplate) {
    super(jdbcTemplate);
  }

  @Override
  @Transactional
  public int save(Invoice invoice) {
    GeneratedKeyHolder buyerKeyHolder = new GeneratedKeyHolder();
    GeneratedKeyHolder sellerKeyHolder = new GeneratedKeyHolder();
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

    // should check if company existed before
    long buyerId = insertCompany(invoice.getBuyer());
    long sellerId = insertCompany(invoice.getSeller());

    jdbcTemplate.update(connection -> {
      PreparedStatement ps =
          connection.prepareStatement("insert into invoice (date, number, buyer, seller) values (?, ?, ?, ?);", new String[] {"id"});
      ps.setDate(1, Date.valueOf(invoice.getDate()));
      ps.setString(2, invoice.getNumber());
      ps.setLong(3, buyerId);
      ps.setLong(4, sellerId);
      return ps;
    }, keyHolder);

    addEntriesRelatedToInvoice(keyHolder.getKey().intValue(), invoice);

    return keyHolder.getKey().intValue();
  }

  @Override
  public Optional<Invoice> getById(int id) {
    List<Invoice> invoices = invoiceRowMapper(id);
    return invoices.isEmpty() ? Optional.empty() : Optional.of(invoices.get(0));
  }

  @Override
  public List<Invoice> getAll() {
    return invoiceRowMapper(null);
  }

  @Override
  public Optional<Invoice> update(int id, Invoice updatedInvoice) {
    try {
      Invoice originalInvoice = invoiceRowMapper(id).get(0);

      updatedInvoice.getBuyer().setId(originalInvoice.getBuyer().getId());
      updateCompany(updatedInvoice.getBuyer());

      updatedInvoice.getSeller().setId(originalInvoice.getSeller().getId());
      updateCompany(updatedInvoice.getSeller());

      jdbcTemplate.update(connection -> {
        PreparedStatement ps =
            connection.prepareStatement(
                "update invoice "
                    + "set date=?, "
                    + "number=? "
                    + "where id=?"
            );
        ps.setDate(1, Date.valueOf(updatedInvoice.getDate()));
        ps.setString(2, updatedInvoice.getNumber());
        ps.setInt(3, id);
        return ps;
      });

      deleteEntriesAndCarsRelatedToInvoice(id);
      addEntriesRelatedToInvoice(id, updatedInvoice);

      return Optional.of(originalInvoice);
    } catch (Exception exception) {
      throw new IllegalArgumentException("Couldn't update invoice with id: " + id, exception.getCause());
    }
  }

  @Override
  public void delete(int id) {
    List<Invoice> invoices = invoiceRowMapper(id);
    if (invoices.isEmpty()) {
      throw new RuntimeException("Could not delete invoice with id: " + id);
    }
    Invoice invoice = invoices.get(0);
    deleteEntriesAndCarsRelatedToInvoice(id);
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "delete from invoice where id = ?;");
      ps.setInt(1, id);
      return ps;
    });
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "delete from company where id in (?, ?);");
      ps.setInt(1, invoice.getBuyer().getId());
      ps.setInt(2, invoice.getSeller().getId());
      return ps;
    });
  }

  private List<Invoice> invoiceRowMapper(Integer id) {
    String baseQuery = "select i.id, i.date, i.number, "
        + "c1.name as seller_name, c1.address as seller_address, "
        + "c1.id as seller_id, c1.tax_identification_number as seller_tax_identification, c1.pension_insurance as seller_pension_insurance,"
        + "c1.health_insurance as seller_health_insurance, "
        + "c2.id as buyer_id, c2.name as buyer_name, c2.address as buyer_address, "
        + "c2.tax_identification_number as buyer_tax_identification, c2.pension_insurance as buyer_pension_insurance, "
        + "c2.health_insurance as buyer_health_insurance "
        + "from invoice i "
        + "inner join company c1 on i.seller = c1.id "
        + "inner join company c2 on i.buyer = c2.id";

    if (id != null) {
      baseQuery += " where i.id ='" + id + "'";
    }

    return jdbcTemplate.query(
        baseQuery,
        (rs, rowNr) -> {
          int invoiceId = rs.getInt("id");

          List<InvoiceEntry> invoiceEntries = jdbcTemplate.query(
              "select * from invoice_invoice_entry iie "
                  + "inner join invoice_entry e on iie.invoice_entry_id = e.id "
                  + "left outer join car c on e.expense_related_to_car = c.id "
                  + "where invoice_id = " + invoiceId,
              (response, ignored) -> InvoiceEntry.builder()
                  .id(response.getInt("id"))
                  .description(response.getString("description"))
                  .quantity(response.getInt("quantity"))
                  .price(response.getBigDecimal("price"))
                  .vatValue(response.getBigDecimal("vat_value"))
                  .vatRate(idToVat(response.getInt("vat_rate")))
                  .depreciationCosts(response.getObject("registration_number") != null
                      ? Car.builder()
                      .registrationNum(response.getString("registration_number"))
                      .ifPrivateUse(response.getBoolean("personal_use"))
                      .build()
                      : null)
                  .build());

          return Invoice.builder()
              .id(invoiceId)
              .date(rs.getDate("date").toLocalDate())
              .number(rs.getString("number"))
              .buyer(Company.builder()
                  .id(rs.getInt("buyer_id"))
                  .name(rs.getString("buyer_name"))
                  .address(rs.getString("buyer_address"))
                  .taxIdentificationNumber(rs.getString("buyer_tax_identification"))
                  .healthInsurance(rs.getBigDecimal("buyer_health_insurance"))
                  .pensionInsurance(rs.getBigDecimal("buyer_pension_insurance"))
                  .build())
              .seller(Company.builder()
                  .id(rs.getInt("seller_id"))
                  .name(rs.getString("seller_name"))
                  .address(rs.getString("seller_address"))
                  .taxIdentificationNumber(rs.getString("seller_tax_identification"))
                  .healthInsurance(rs.getBigDecimal("seller_health_insurance"))
                  .pensionInsurance(rs.getBigDecimal("seller_pension_insurance"))
                  .build())
              .entries(invoiceEntries)
              .build();
        });
  }

  private int addEntriesRelatedToInvoice(int invoiceId, Invoice invoice) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    invoice.getEntries().forEach(entry -> {
      jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(
            "insert into invoice_entry (description, quantity, price, vat_value, vat_rate, expense_related_to_car) values (?, ?, ?, ?, ?,?);",
            new String[] {"id"});
        ps.setString(1, entry.getDescription());
        ps.setInt(2, entry.getQuantity());
        ps.setBigDecimal(3, entry.getPrice());
        ps.setBigDecimal(4, entry.getVatValue());
        ps.setInt(5, getVatRateId(entry.getVatRate()));
        ps.setInt(6, getCarId(entry.getDepreciationCosts()));
        return ps;
      }, keyHolder);

      int invoiceEntryId = keyHolder.getKey().intValue();

      jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(
            "insert into invoice_invoice_entry (invoice_id, invoice_entry_id) values (?, ?);");
        ps.setInt(1, invoiceId);
        ps.setInt(2, invoiceEntryId);
        return ps;
      });
    });

    return invoiceId;
  }

  private int getVatRateId(Vat vat) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

    List<Integer> vatRateIds = jdbcTemplate.query("select * from public.vat where name like '" + vat.name() + "'",
        (rs, rowNum) -> rs.getInt("id"));

    return vatRateIds.get(0);

  }

  private int getCarId(Car car) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps =
          connection.prepareStatement("insert into car (registration_number, personal_use) values (?, ?);", new String[] {"id"});
      ps.setString(1, car.getRegistrationNum());
      ps.setBoolean(2, car.getIfPrivateUse());
      return ps;
    }, keyHolder);

    return keyHolder.getKey().intValue();

  }

  private Vat idToVat(int id) {
    String vatRateName = jdbcTemplate.queryForObject("select name from vat where id = ?", new Object[] {id}, String.class);

    return vatRateName != null ? Vat.valueOf(vatRateName) : null;
  }

  private void deleteEntriesAndCarsRelatedToInvoice(int id) {
    System.out.println(id);
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement("delete from car where id in ("
          + "select expense_related_to_car from invoice_entry where id in ("
          + "select invoice_entry_id from invoice_invoice_entry where invoice_id=?));");
      ps.setInt(1, id);
      return ps;
    });

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "delete from invoice_entry where id in (select invoice_entry_id from invoice_invoice_entry where invoice_id=?);");
      ps.setInt(1, id);
      return ps;
    });
  }

}
