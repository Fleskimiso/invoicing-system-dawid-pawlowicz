package pl.futurecollars.invoicing.db;

import java.util.List;
import java.util.Optional;
import pl.futurecollars.invoicing.model.Invoice;

public interface Database {

  int save(Invoice invoice);

  Optional<Invoice> getById(int id);

  List<Invoice> getAll();

  Optional<Invoice> update(int id, Invoice updatedInvoice);

  void delete(int id);

}
