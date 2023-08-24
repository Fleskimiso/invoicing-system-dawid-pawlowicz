package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.IdService;
import pl.futurecollars.invoicing.utils.FileService;
import pl.futurecollars.invoicing.utils.JsonService;

@Configuration
@Slf4j
@ConditionalOnProperty(name = "invoicing-system.database.type", havingValue = "file")
public class FileBasedDatabaseConfiguration {

  @Bean
  public IdService idService(FileService fileService,
                             @Value("${invoicing-system.database.location}") String databaseLocation,
                             @Value("${invoicing-system.database.id.filename}") String idFilename
  ) throws IOException {
    Path idFilePath = Files.createTempFile(databaseLocation, idFilename);
    return new IdService(idFilePath);
  }

  @Bean
  @Primary
  @Qualifier("invoice")
  public Database<Invoice> invoicesFileBasedDatabase(IdService idService,
                                                     JsonService jsonService,
                                                     FileService fileService,
                                                     @Value("${invoicing-system.database.location}") String databaseLocation,
                                                     @Value("${invoicing-system.database.invoices.filename}") String invoicesFilename
  ) throws IOException {
    log.info("creating invoices filebased database");
    Path databasePath = Files.createTempFile(databaseLocation, invoicesFilename);
    return new FileBasedDatabase<>(databasePath, idService, jsonService, fileService, Invoice.class);
  }

  @Bean
  @Qualifier("company")
  public Database<Company> companyFileBasedDatabase(IdService idService,
                                                    JsonService jsonService,
                                                    FileService fileService,
                                                    @Value("${invoicing-system.database.location}") String databaseLocation,
                                                    @Value("${invoicing-system.database.company.filename}") String companyFilename
  ) throws IOException {
    log.info("creating company filebased database");
    Path databasePath = Files.createTempFile(databaseLocation, companyFilename);
    return new FileBasedDatabase<>(databasePath, idService, jsonService, fileService, Company.class);
  }
}
