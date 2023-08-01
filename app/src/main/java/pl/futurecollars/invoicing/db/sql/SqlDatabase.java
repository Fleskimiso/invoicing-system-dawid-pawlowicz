package pl.futurecollars.invoicing.db.sql;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.annotation.Transactional;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Car;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.Vat;

@AllArgsConstructor
public class SqlDatabase implements Database {

  private JdbcTemplate jdbcTemplate;

  private final Map<Vat, Integer> vatToId = new HashMap<>();
  private final Map<Integer, Vat> idToVat = new HashMap<>();

  @Override
  @Transactional
  public int save(Invoice invoice) {
    int buyerId = insertCompany(invoice.getBuyer());
    int sellerId = insertCompany(invoice.getSeller());

    int invoiceId = insertInvoice(invoice, buyerId, sellerId);
    addEntriesToInvoice(invoiceId, invoice);
    return 0;
  }

  @Override
  public Optional<Invoice> getById(int id) {
    return Optional.empty();
  }

  @Override
  public List<Invoice> getAll() {
    jdbcTemplate.query(" select * from invoice" , rs ->  {
      System.out.println(rs.getDate("date"));
      System.out.println(rs.getString("number"));
      System.out.println(rs.getInt("buyer"));
      System.out.println(rs.getInt("seller"));
    });
    return null;
  }

  @Override
  public Optional<Invoice> update(int id, Invoice updatedInvoice) {
    return Optional.empty();
  }

  @Override
  public void delete(int id) {

  }

  private int insertCompany(Company company){
    GeneratedKeyHolder gen = new GeneratedKeyHolder();

    jdbcTemplate.update(connection ->{
      PreparedStatement prepare = connection.prepareStatement(
          "insert into company (address, name, tax_identification_number, health_insurance, pension_insurance) values (?, ?, ?, ?, ?);",
          new String[] {"id"}
      );
      prepare.setString(2, company.getAddress());
      prepare.setString(1, company.getName());
      prepare.setString(3, company.getTaxIdentificationNumber());
      prepare.setBigDecimal(4, company.getHealthInsurance());
      prepare.setBigDecimal(5, company.getPensionInsurance());
      return prepare;
    },gen);
    return Objects.requireNonNull(gen.getKey()).intValue();
  }

  private int insertInvoice(Invoice invoice, int buyerId, int sellerId) {
    GeneratedKeyHolder gen = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement prepare =
          connection.prepareStatement("insert into invoice (date, buyer, seller, number) values (?, ?, ?, ?);", new String[] {"id"});
      prepare.setDate(1, Date.valueOf(invoice.getDate()));
      prepare.setLong(3, buyerId);
      prepare.setLong(4, sellerId);
      prepare.setString(2, invoice.getNumber());
      return prepare;
    }, gen);

    return Objects.requireNonNull(gen.getKey()).intValue();
  }

  private void addEntriesToInvoice(int invoiceId, Invoice invoice) {
    GeneratedKeyHolder gen = new GeneratedKeyHolder();
    invoice.getEntries().forEach(entry -> {
      jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection
            .prepareStatement(
                "insert into invoice_entry (description, quantity, price, vat_value, vat_rate, depreciationCosts) "
                    + "values (?, ?, ?, ?, ?, ?);",
                new String[] {"id"});
        ps.setString(1, entry.getDescription());
        ps.setInt(2, entry.getQuantity());
        ps.setBigDecimal(3, entry.getPrice());
        ps.setBigDecimal(4, entry.getVatValue());
        ps.setInt(5, vatToId.get(entry.getVatRate()));
        ps.setObject(6, insertCarAndGetItId(entry.getDepreciationCosts()));
        return ps;
      }, gen);

      int invoiceEntryId = Objects.requireNonNull(gen.getKey()).intValue();

      jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(
            "insert into invoice_invoice_entry (invoice_id, invoice_entry_id) values (?, ?);");
        ps.setInt(1, invoiceId);
        ps.setInt(2, invoiceEntryId);
        return ps;
      });
    });
  }

  private Integer insertCarAndGetItId(Car car) {
    if (car == null) {
      return null;
    }

    GeneratedKeyHolder gen = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection
          .prepareStatement(
              "insert into car (registration_number, personal_use) values (?, ?);",
              new String[] {"id"});
      ps.setString(1, car.getRegistrationNum());
      ps.setBoolean(2, car.getIfPrivateUse());
      return ps;
    }, gen);

    return Objects.requireNonNull(gen.getKey()).intValue();
  }

  @PostConstruct
  void initVatRatesMap() { // default so it can be called from SqlDatabaseIntegrationTest
    jdbcTemplate.query("select * from vat",
        rs -> {
          Vat vat = Vat.valueOf("VAT_" + rs.getString("name"));
          int id = rs.getInt("id");
          vatToId.put(vat, id);
          idToVat.put(id, vat);
        });
  }

}
