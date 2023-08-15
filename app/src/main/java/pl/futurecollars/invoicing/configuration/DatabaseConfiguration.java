package pl.futurecollars.invoicing.configuration;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.db.file.FileBasedDatabase;
import pl.futurecollars.invoicing.db.memory.InMemoryDatabase;
import pl.futurecollars.invoicing.db.nosql.MongoBasedDatabase;
import pl.futurecollars.invoicing.db.sql.SqlDatabase;
import pl.futurecollars.invoicing.db.sql.jpa.InvoiceRepository;
import pl.futurecollars.invoicing.db.sql.jpa.JpaDatabase;
import pl.futurecollars.invoicing.model.Invoice;
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

  @ConditionalOnProperty(name = "invoicing-system.database.type", havingValue = "sql")
  @Bean
  public Database sqlDatabase(JdbcTemplate jdbcTemplate) {
    log.info("Creating sql database");
    return new SqlDatabase(jdbcTemplate);
  }

  @ConditionalOnProperty(name = "invoicing-system.database.type", havingValue = "jpa")
  @Bean
  @Primary
  public Database jpaDatabase(InvoiceRepository invoiceRepository) {
    return new JpaDatabase(invoiceRepository);
  }

  @ConditionalOnProperty(name = "invoicing-system.database.type", havingValue = "mongo")
  @Bean
  public Database mongoDatabase(
      @Value("${invoicing-system.database.name}") String databaseName,
      @Value("${invoicing-system.database.collection}") String collectionName
      ) {
    CodecRegistry pojoCodeRegistry = CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
        CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    MongoClientSettings settings = MongoClientSettings.builder()
            .codecRegistry(pojoCodeRegistry)
                .build();

    log.info("Connecting to mongo database");
    MongoClient client = MongoClients.create(settings);
    MongoDatabase database = client.getDatabase(databaseName);
    MongoCollection<Invoice> collection = database.getCollection(collectionName, Invoice.class);
    return new MongoBasedDatabase(collection);
  }

}
