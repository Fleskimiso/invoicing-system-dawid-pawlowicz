package pl.futurecollars.invoicing.db;

import java.util.List;
import java.util.Optional;
import pl.futurecollars.invoicing.model.WithId;

public interface Database<T extends WithId> {

  int save(T item);

  Optional<T> getById(int id);

  List<T> getAll();

  Optional<T> update(int id, T updatedInvoice);

  void delete(int id);

  default void reset() {
    getAll().forEach(item -> delete(item.getId()));
  }

}
