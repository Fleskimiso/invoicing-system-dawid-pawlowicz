package pl.futurecollars.invoicing.db;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;
import pl.futurecollars.invoicing.model.WithId;

public interface Database<T extends WithId> {

  int save(T item);

  Optional<T> getById(int id);

  List<T> getAll();

  Optional<T> update(int id, T updatedInvoice);

  void delete(int id);

//  default BigDecimal visit(Predicate<Invoice> invoicePredicate, Function<InvoiceEntry, BigDecimal> invoiceEntryToValue) {
//    return getAll().stream()
//        .filter(invoicePredicate)
//        .flatMap(i -> i.getEntries().stream())
//        .map(invoiceEntryToValue)
//        .reduce(BigDecimal.ZERO, BigDecimal::add);
//  }

  default void reset() {
    getAll().forEach(item -> delete(item.getId()));
  }

}
