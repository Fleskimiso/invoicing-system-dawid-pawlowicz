package pl.futurecollars.invoicing.db.file;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.FileService;
import pl.futurecollars.invoicing.service.IdService;
import pl.futurecollars.invoicing.service.JsonService;

@AllArgsConstructor
public class FileBasedDatabase implements Database {

  private final Path databasePath;
  private final IdService idService;
  private final JsonService jsonService;
  private final FileService fileService;

  @Override
  public int save(Invoice invoice) {
    try {
      invoice.setId(idService.getNextIdAndSaveIncremented());
      fileService.appendToFile(databasePath, jsonService.toJson(invoice));
      return invoice.getId();
    } catch (IOException exception) {
      throw new RuntimeException("Failed to save the invoice", exception);
    }
  }

  @Override
  public Optional<Invoice> getById(int id) {
    try {
      return fileService.readLinesFromFile(databasePath)
          .stream()
          .filter(line -> containsId(line, id))
          .map(line -> jsonService.toObject(line, Invoice.class))
          .findFirst();
    } catch (IOException exception) {
      throw new RuntimeException("Failed to find invoice with given id: " + id, exception);
    }
  }

  @Override
  public List<Invoice> getAll() {
    try {
      return fileService.readLinesFromFile(databasePath)
          .stream()
          .map(line -> jsonService.toObject(line, Invoice.class))
          .collect(Collectors.toList());
    } catch (IOException exception) {
      throw new RuntimeException("Failed to read invoices from file", exception);
    }
  }

  @Override
  public Optional<Invoice> update(int id, Invoice updatedInvoice) {
    try {
      List<String> allInvoices = fileService.readLinesFromFile(databasePath);
      List<String> filteredInvoices = allInvoices
          .stream()
          .filter(line -> !containsId(line, id))
          .collect(Collectors.toList());

      if (filteredInvoices.size() == allInvoices.size()) {
        throw new IllegalArgumentException("No record with such id exists: " + id);
      }

      updatedInvoice.setId(id);
      filteredInvoices.add(jsonService.toJson(updatedInvoice));

      fileService.writeLinesToFile(databasePath, filteredInvoices);

      return Optional.of(updatedInvoice);
    } catch (IOException exception) {
      throw new RuntimeException("Failed to update invoice with id: " + id, exception);
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

  public boolean containsId(String line, int id) {
    return line.contains("\"id\":" + id);
  }

}
