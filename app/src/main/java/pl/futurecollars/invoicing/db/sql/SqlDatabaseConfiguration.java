package pl.futurecollars.invoicing.db.sql;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;

@Configuration
@Slf4j
@ConditionalOnProperty(name = "invoicing-system.database.type", havingValue = "sql")
public class SqlDatabaseConfiguration {

  @Bean
  @Primary
  @Qualifier("invoice")
  public Database<Invoice> invoiceSqlDatabase(JdbcTemplate jdbcTemplate) {
    log.info("creating invoices sql database");
    return new InvoiceSqlDatabase(jdbcTemplate);
  }

  @Bean
  @Qualifier("company")
  public Database<Company> companySqlDatabase(JdbcTemplate jdbcTemplate) {
    log.info("creating company sql database");
    return new CompanySqlDatabase(jdbcTemplate);
  }

}
