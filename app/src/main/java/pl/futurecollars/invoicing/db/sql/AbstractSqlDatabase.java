package pl.futurecollars.invoicing.db.sql;

import java.sql.PreparedStatement;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import pl.futurecollars.invoicing.model.Company;

@AllArgsConstructor
public abstract class AbstractSqlDatabase {

  protected final JdbcTemplate jdbcTemplate;

  protected int insertCompany(Company buyer) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "insert into company (name, address, tax_identification_number, health_insurance, pension_insurance) values (?, ?, ?, ?, ?);",
          new String[] {"id"});
      ps.setString(1, buyer.getName());
      ps.setString(2, buyer.getAddress());
      ps.setString(3, buyer.getTaxIdentificationNumber());
      ps.setBigDecimal(4, buyer.getHealthInsurance());
      ps.setBigDecimal(5, buyer.getPensionInsurance());
      return ps;
    }, keyHolder);

    return keyHolder.getKey().intValue();
  }

  protected void updateCompany(Company company) {
    jdbcTemplate.update(connection -> {
      PreparedStatement ps = connection.prepareStatement(
          "update company "
              + "set name=?, "
              + "address=?, "
              + "tax_identification_number=?, "
              + "health_insurance=?, "
              + "pension_insurance=? "
              + "where id=?"
      );
      ps.setString(1, company.getName());
      ps.setString(2, company.getAddress());
      ps.setString(3, company.getTaxIdentificationNumber());
      ps.setBigDecimal(4, company.getHealthInsurance());
      ps.setBigDecimal(5, company.getPensionInsurance());
      ps.setLong(6, company.getId());
      return ps;
    });
  }
}
