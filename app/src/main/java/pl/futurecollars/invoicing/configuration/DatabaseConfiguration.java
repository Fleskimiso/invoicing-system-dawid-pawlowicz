package pl.futurecollars.invoicing.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.file.FileBasedDatabase;
import pl.futurecollars.invoicing.db.memory.InMemoryDatabase;
import pl.futurecollars.invoicing.service.IdService;
import pl.futurecollars.invoicing.utils.FileService;
import pl.futurecollars.invoicing.utils.JsonService;

@Slf4j
@Configuration
public class DatabaseConfiguration {

  @Bean
  public IdService idService(FileService fileService,
                             @Value("${invoicing-system.database.location}") String databaseLocation,
                             @Value("${invoicing-system.database.id.filename}") String idFilename
  ) throws IOException {
    Path idFilePath = Files.createTempFile(databaseLocation, idFilename);
    return new IdService(idFilePath);
  }

  @ConditionalOnProperty(name = "invoicing-system.database.type", havingValue = "file")
  @Bean
  public FileBasedDatabase fileBasedDatabase(IdService idService,
                                             JsonService jsonService,
                                             FileService fileService,
                                             @Value("${invoicing-system.database.location}") String databaseLocation,
                                             @Value("${invoicing-system.database.invoices.filename}") String databaseFilename
  ) throws IOException {
    log.debug("Creating fileBasedDatabase");
    Path databasePath = Files.createTempFile(databaseLocation, databaseFilename);
    return new FileBasedDatabase(databasePath, idService, jsonService, fileService);
  }

  @ConditionalOnProperty(name = "invoicing-system.database.type", havingValue = "memory")
  @Bean
  public InMemoryDatabase inMemoryDatabase() {
    log.debug("Creating inMemoryDatabase ");
    return new InMemoryDatabase();
  }

}
