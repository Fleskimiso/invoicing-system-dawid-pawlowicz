package pl.futurecollars.invoicing.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

@Service
public class InvoiceService {

  private final Database database;

  public InvoiceService(Database database) {
    this.database = database;
  }

  public int save(Invoice invoice) {
    return database.save(invoice);
  }

  public Optional<Invoice> getById(int id) {
    return database.getById(id);
  }

  public Optional<Invoice> update(int id, Invoice updatedInvoice) {
    return database.update(id, updatedInvoice);
  }

  public List<Invoice> getAll() {
    return database.getAll();
  }

  public void delete(int id) {
    database.delete(id);
  }

}
