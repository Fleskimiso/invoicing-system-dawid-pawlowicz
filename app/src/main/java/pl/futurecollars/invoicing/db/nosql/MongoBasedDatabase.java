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

@AllArgsConstructor
public class MongoBasedDatabase implements Database {

  private final MongoCollection<Invoice> collection;
  private final MongoIdProvider idProvider;

  @Override
  public int save(Invoice invoice) {
    invoice.setId((int) idProvider.getNextIdAndIncrement());
    collection.insertOne(invoice);
    return 0;
  }

  @Override
  public Optional<Invoice> getById(int id) {
    return Optional.ofNullable(
        collection.find(new Document("_id", id)).first());
  }

  @Override
  public List<Invoice> getAll() {
    return StreamSupport
        .stream(collection.find().spliterator(), false)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<Invoice> update(int id, Invoice updatedInvoice) {
    updatedInvoice.setId(id);
    return Optional.ofNullable(
        collection.findOneAndReplace(idFilter(id), updatedInvoice)
    );
  }

  @Override
  public void delete(int id) {
    collection.findOneAndDelete(idFilter(id));
  }

  private Document idFilter(long id) {
    return new Document("_id", id);
  }

}
