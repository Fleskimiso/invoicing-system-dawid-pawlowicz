package pl.futurecollars.invoicing.db.sql.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;

@Configuration
@Slf4j
@ConditionalOnProperty(name = "invoicing-system.database.type", havingValue = "jpa")
public class JpaDatabaseConfiguration {

  @Bean
  @Primary
  @Qualifier("invoice")
  public Database<Invoice> invoiceJpaDatabase(InvoiceRepository repository) {
    log.info("Creating invoices jpaDatabase");
    return new JpaDatabase<>(repository);
  }

  @Bean
  @Qualifier("company")
  public Database<Company> companyJpaDatabase(CompanyRepository repository) {
    log.info("Creating company jpaDatabase");
    return new JpaDatabase<>(repository);
  }

}
