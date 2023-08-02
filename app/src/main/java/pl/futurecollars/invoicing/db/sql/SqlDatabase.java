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

  @Override
  @Transactional
  public int save(Invoice invoice) {
    GeneratedKeyHolder buyerKeyHolder = new GeneratedKeyHolder();
    GeneratedKeyHolder sellerKeyHolder = new GeneratedKeyHolder();
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement("insert into company (name, address, tax_identification_number) values (?, ?, ?);", new String[]{"id"});
      ps.setString(1, invoice.getBuyer().getName());
      ps.setString(2, invoice.getBuyer().getAddress());
      ps.setString(3, invoice.getBuyer().getTaxIdentificationNumber());
      return ps;
    }, buyerKeyHolder);

    long buyerId = buyerKeyHolder.getKey().longValue();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement("insert into company (name, address, tax_identification_number) values (?, ?, ?);", new String[]{"id"});
      ps.setString(1, invoice.getSeller().getName());
      ps.setString(2, invoice.getSeller().getAddress());
      ps.setString(3, invoice.getSeller().getTaxIdentificationNumber());
      return ps;
    }, sellerKeyHolder);

    long sellerId = sellerKeyHolder.getKey().longValue();

    /*addEntriesRelatedToInvoice(invoice.getId(), invoice);*/

    jdbcTemplate.update(connection -> {
      PreparedStatement ps =
          connection.prepareStatement( "insert into invoice (date, number, buyer, seller) values (?, ?, ?, ?);", new String[] {"id"});
              ps.setDate( 1, Date.valueOf(invoice.getDate()));
              ps.setString( 2, invoice.getNumber());
              ps.setLong( 3, buyerId);
              ps.setLong( 4, sellerId);
      return ps;
      },   keyHolder);

      return keyHolder.getKey().intValue();
  }

  @Override
  public Optional<Invoice> getById(int id) {
    return Optional.empty();
  }

  @Override
  public List<Invoice> getAll() {
    return jdbcTemplate.query( "select i.date, i.number, c1.name as seller_name, c2.name as buyer_name from invoice i "
            + "inner join company c1 on i.seller = c1.id "
            + "inner join company c2 on i.buyer = c2.id",
        (rs, rowNr) ->
            Invoice.builder()
                .date(rs.getDate( "date").toLocalDate())
                .number(rs.getString( "number"))
                .buyer(Company.builder().name(rs.getString( "buyer_name")).build())
                .seller(Company.builder().name(rs.getString( "seller_name")).build())
                .build());
  }

  @Override
  public Optional<Invoice> update(int id, Invoice updatedInvoice) {
    return Optional.empty();
  }

  @Override
  public void delete(int id) {

  }

  private int addEntriesRelatedToInvoice(int invoiceId, Invoice invoice) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    invoice.getEntries().forEach(entry -> {
      jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(
            "insert into invoice_entry (description, quantity, price, vat_value, vat_rate) values (?, ?, ?, ?, ?);",
            new String[] {"id"});
        ps.setString(1, entry.getDescription());
        ps.setInt(2, entry.getQuantity());
        ps.setBigDecimal(3, entry.getPrice());
        ps.setBigDecimal(4, entry.getVatValue());
        ps.setInt(5, 1);
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

}
