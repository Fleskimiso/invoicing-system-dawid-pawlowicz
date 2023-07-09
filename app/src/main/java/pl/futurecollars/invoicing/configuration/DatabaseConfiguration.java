package pl.futurecollars.invoicing.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.futurecollars.invoicing.db.file.FileBasedDatabase;
import pl.futurecollars.invoicing.service.IdService;
import pl.futurecollars.invoicing.utils.FileService;
import pl.futurecollars.invoicing.utils.JsonService;

@Configuration
public class DatabaseConfiguration {

  public static final String DATABASE_LOCATION = "db";
  public static final String DATABASE_FILENAME = "records.json";
  public static final String ID_FILENAME = "id.txt";

  @Bean
  public IdService idService(FileService fileService) throws IOException {
    Path idFilePath = Files.createTempFile(DATABASE_LOCATION, ID_FILENAME);
    return new IdService(idFilePath);
  }

  @Bean
  public FileBasedDatabase fileBasedDatabase(IdService idService, JsonService jsonService, FileService fileService) throws IOException {
    Path databasePath = Files.createTempFile(DATABASE_LOCATION, DATABASE_FILENAME);
    return new FileBasedDatabase(databasePath, idService, jsonService, fileService);
  }

}
