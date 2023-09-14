package pl.futurecollars.invoicing.db.nosql;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;

@ConditionalOnProperty(name = "invoicing-system.database.type", havingValue = "mongo")
@Slf4j
@Configuration
public class MongoBasedDatabaseConfiguration {

  @Bean
  public MongoDatabase mongoDb(
      @Value("${invoicing-system.database.name}") String databaseName
  ) {
    CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
        fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    MongoClientSettings settings = MongoClientSettings.builder()
        .codecRegistry(pojoCodecRegistry)
        .build();

    MongoClient client = MongoClients.create(settings);
    return client.getDatabase(databaseName);
  }

  @Bean
  public MongoIdProvider mongoIdProvider(
      @Value("${invoicing-system.database.counter.collection}") String collectionName,
      MongoDatabase mongoDb
  ) {
    MongoCollection<Document> collection = mongoDb.getCollection(collectionName);
    return new MongoIdProvider(collection);
  }

  @Bean
  @Primary
  @Qualifier("invoice")
  public Database<Invoice> invoiceMongoDatabase(
      @Value("${invoicing-system.database.invoices.collection}") String collectionName,
      MongoDatabase mongoDb,
      MongoIdProvider mongoIdProvider
  ) {
    MongoCollection<Invoice> collection = mongoDb.getCollection(collectionName, Invoice.class);
    log.info("Creating invoices mongoDatabase");
    return new MongoBasedDatabase<>(collection, mongoIdProvider);
  }

  @Bean
  @Qualifier("company")
  public Database<Company> companyMongoDatabase(
      @Value("${invoicing-system.database.companies.collection}") String collectionName,
      MongoDatabase mongoDb,
      MongoIdProvider mongoIdProvider
  ) {
    MongoCollection<Company> collection = mongoDb.getCollection(collectionName, Company.class);
    log.info("Creating company mongoDatabase");
    return new MongoBasedDatabase<>(collection, mongoIdProvider);
  }

}
