package pl.futurecollars.invoicing.db.nosql;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
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
public class MongoBasedDatabaseCompany<Company extends WithId> implements Database<Company> {

  private final MongoCollection<Company> collection;
  private final MongoCollection<Invoice> invoiceCollection;
  private final MongoIdProvider idProvider;

  @Override
  public int save(Company item) {
    item.setId((int) idProvider.getNextIdAndIncrement());
    collection.insertOne(item);
    return item.getId();
  }

  @Override
  public Optional<Company> getById(int id) {
    return Optional.ofNullable(
        collection.find(new Document("_id", id)).first());
  }

  @Override
  public List<Company> getAll() {
    return StreamSupport
        .stream(collection.find().spliterator(), false)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<Company> update(int id, Company updatedItem) {
    if (getById(id).isPresent()) {
      updatedItem.setId(id);

      Optional<Company> updatedCompany = Optional.ofNullable(
          collection.findOneAndReplace(idFilter(id), updatedItem)
      );

      if (updatedCompany.isPresent()) {
        updateCompanyInInvoices(id, updatedItem);
      }

      return updatedCompany;

    } else {
      throw new IllegalArgumentException("Couldn't update item with id: " + id);
    }

  }

  private void updateCompanyInInvoices(int companyId, Company updatedCompany) {
    // Update buyer name in invoices
    invoiceCollection.updateMany(Filters.eq("buyer._id", companyId),
        new Document("$set", new Document("buyer", updatedCompany)));

    // Update seller name in invoices
    invoiceCollection.updateMany(Filters.eq("seller._id", companyId),
        new Document("$set", new Document("seller", updatedCompany)));
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
