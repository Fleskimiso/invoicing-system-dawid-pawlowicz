package pl.futurecollars.invoicing.db.nosql;

import com.mongodb.client.MongoCollection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import org.bson.Document;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.WithId;

@AllArgsConstructor
public class MongoBasedDatabase<T extends WithId> implements Database<T> {

  private final MongoCollection<T> collection;
  private final MongoIdProvider idProvider;

  @Override
  public int save(T item) {
    item.setId((int) idProvider.getNextIdAndIncrement());
    collection.insertOne(item);
    return item.getId();
  }

  @Override
  public Optional<T> getById(int id) {
    return Optional.ofNullable(
        collection.find(new Document("_id", id)).first());
  }

  @Override
  public List<T> getAll() {
    return StreamSupport
        .stream(collection.find().spliterator(), false)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<T> update(int id, T updatedItem) {
    if (getById(id).isPresent()) {
      updatedItem.setId(id);
      return Optional.ofNullable(
          collection.findOneAndReplace(idFilter(id), updatedItem)
      );
    } else {
      throw new IllegalArgumentException("Couldn't update item with id: " + id);
    }

  }

  @Override
  public void delete(int id) {
    if (getById(id).isPresent()) {
      collection.findOneAndDelete(idFilter(id));
    } else {
      throw new RuntimeException("No item is present with id: " + id);
    }
  }

  private Document idFilter(long id) {
    return new Document("_id", id);
  }

}
