package pl.futurecollars.invoicing.db.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

public class InMemoryDatabase implements Database {

  private final HashMap<Integer, Invoice> records = new HashMap<>();
  private int currentId = 1;

  @Override
  public int save(Invoice invoice) {
    invoice.setId(currentId);
    records.put(currentId, invoice);
    currentId += 1;
    return currentId - 1;
  }

  @Override
  public Optional<Invoice> getById(int id) {
    return Optional.ofNullable(records.get(id));
  }

  @Override
  public List<Invoice> getAll() {
    return new ArrayList<>(records.values());
  }

  @Override
  public void update(int id, Invoice updatedInvoice) {
    if (!records.containsKey(id)) {
      throw new IllegalArgumentException("No element exists with such id: " + id);
    }

    updatedInvoice.setId(id);
    records.put(id, updatedInvoice);
  }

  @Override
  public void delete(int id) {
    records.remove(id);
  }
}
