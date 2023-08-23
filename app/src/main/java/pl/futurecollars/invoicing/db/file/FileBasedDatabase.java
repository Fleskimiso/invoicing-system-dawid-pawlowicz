package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.WithId;
import pl.futurecollars.invoicing.service.IdService;
import pl.futurecollars.invoicing.utils.FileService;
import pl.futurecollars.invoicing.utils.JsonService;

@AllArgsConstructor
public class FileBasedDatabase<T extends WithId> implements Database<T> {

  private final Path databasePath;
  private final IdService idService;
  private final JsonService jsonService;
  private final FileService fileService;
  private final Class<T> clazz;

  @Override
  public int save(T item) {
    try {
      item.setId(idService.getNextIdAndSaveIncremented());
      fileService.appendToFile(databasePath, jsonService.toJson(item));
      return item.getId();
    } catch (IOException exception) {
      throw new RuntimeException("Failed to save the item", exception);
    }
  }

  @Override
  public Optional<T> getById(int id) {
    try {
      return fileService.readLinesFromFile(databasePath)
          .stream()
          .filter(line -> containsId(line, id))
          .map(line -> jsonService.toObject(line, clazz))
          .findFirst();
    } catch (IOException exception) {
      throw new RuntimeException("Failed to find item with given id: " + id, exception);
    }
  }

  @Override
  public List<T> getAll() {
    try {
      return fileService.readLinesFromFile(databasePath)
          .stream()
          .map(line -> jsonService.toObject(line, clazz))
          .collect(Collectors.toList());
    } catch (IOException exception) {
      throw new RuntimeException("Failed to read item from file", exception);
    }
  }

  @Override
  public Optional<T> update(int id, T updatedItem) {
    try {
      List<String> allRecords = fileService.readLinesFromFile(databasePath);
      List<String> filteredRecords = allRecords
          .stream()
          .filter(line -> !containsId(line, id))
          .collect(Collectors.toList());

      if (filteredRecords.size() == allRecords.size()) {
        throw new IllegalArgumentException("No record with such id exists: " + id);
      }

      updatedItem.setId(id);
      filteredRecords.add(jsonService.toJson(updatedItem));

      fileService.writeLinesToFile(databasePath, filteredRecords);

      return Optional.of(updatedItem);
    } catch (IOException exception) {
      throw new RuntimeException("Failed to update item with id: " + id, exception);
    }

  }

  @Override
  public void delete(int id) {
    try {
      List<String> originalList = fileService.readLinesFromFile(databasePath).stream().toList();
      List<String> updatedList = originalList
          .stream()
          .filter(line -> !containsId(line, id))
          .collect(Collectors.toList());

      fileService.writeLinesToFile(databasePath, updatedList);

      if (originalList.size() == updatedList.size()) {
        throw new RuntimeException("No record has been deleted with id: " + id);
      }

    } catch (IOException ex) {
      throw new RuntimeException("Failed to delete invoice with id: " + id, ex);
    }
  }

  private boolean containsId(String line, int id) {
    return line.contains("\"id\":" + id);
  }

}
