package pl.futurecollars.invoicing.db.memory;

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
@ConditionalOnProperty(name = "invoicing-system.database.type", havingValue = "memory")
public class InMemoryDatabaseConfiguration {

  @Bean
  @Qualifier("invoice")
  @Primary
  public Database<Invoice> invoiceInMemoryDatabase() {
    log.info("Creating in memory invoice database");
    return new InMemoryDatabase<Invoice>();
  }

  @Bean
  @Qualifier("company")
  public Database<Company> companyInMemoryDatabase() {
    log.info("Creating in memory company database");
    return new InMemoryDatabase<Company>();
  }

}
