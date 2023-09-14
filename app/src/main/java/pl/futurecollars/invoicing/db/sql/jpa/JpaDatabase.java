package pl.futurecollars.invoicing.db.sql.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.WithId;

@AllArgsConstructor
public class JpaDatabase<T extends WithId> implements Database<T> {

  private final CrudRepository<T, Integer> repository;

  @Override
  public int save(T item) {
    return repository.save(item).getId();
  }

  @Override
  public Optional<T> getById(int id) {
    return repository.findById(id);
  }

  @Override
  public List<T> getAll() {
    return StreamSupport.stream(repository.findAll().spliterator(), false)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<T> update(int id, T updatedItem) {
    Optional<T> itemOptional = getById(id);

    if (!itemOptional.isPresent()) {
      throw new IllegalArgumentException("Invoice with ID " + id + " does not exist and cannot be updated.");
    }
    updatedItem.setId(id);
    repository.save(updatedItem);

    return itemOptional;
  }

  @Override
  public void delete(int id) {
    Optional<T> item = getById(id);
    if (item.isEmpty()) {
      throw new RuntimeException("Could not delete item with id: " + id);
    }
    item.ifPresent(repository::delete);
  }
}
