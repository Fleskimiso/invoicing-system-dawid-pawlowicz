package pl.futurecollars.invoicing.db.nosql;

import com.mongodb.client.MongoCollection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

@AllArgsConstructor
public class MongoBasedDatabase implements Database {

  private MongoCollection<Invoice> collection;

  @Override
  public int save(Invoice invoice) {
    collection.insertOne(invoice);
    return 0;
  }

  @Override
  public Optional<Invoice> getById(int id) {
    return Optional.empty();
  }

  @Override
  public List<Invoice> getAll() {
    return StreamSupport
        .stream(collection.find().spliterator(),false)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<Invoice> update(int id, Invoice updatedInvoice) {
    return Optional.empty();
  }

  @Override
  public void delete(int id) {

  }

}
