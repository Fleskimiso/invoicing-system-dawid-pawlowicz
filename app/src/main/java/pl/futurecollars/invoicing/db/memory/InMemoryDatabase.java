package pl.futurecollars.invoicing.db.memory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.WithId;

public class InMemoryDatabase<T extends WithId> implements Database<T> {

  private final HashMap<Integer, T> records = new HashMap<>();
  private int currentId = 1;

  @Override
  public int save(T item) {
    item.setId(currentId);
    records.put(currentId, item);
    return currentId++;
  }

  @Override
  public Optional<T> getById(int id) {
    return Optional.ofNullable(records.get(id));
  }

  @Override
  public List<T> getAll() {
    return new ArrayList<>(records.values());
  }

  @Override
  public Optional<T> update(int id, T updatedItem) {
    if (!records.containsKey(id)) {
      throw new IllegalArgumentException("No element exists with such id: " + id);
    }

    updatedItem.setId(id);
    records.put(id, updatedItem);

    return Optional.of(updatedItem);

  }

  @Override
  public void delete(int id) {
    records.remove(id);
  }
}
