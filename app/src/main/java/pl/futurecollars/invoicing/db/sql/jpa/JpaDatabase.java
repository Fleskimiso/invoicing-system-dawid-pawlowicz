package pl.futurecollars.invoicing.db.sql.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.AllArgsConstructor;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Invoice;

@AllArgsConstructor
public class JpaDatabase implements Database {

  private final InvoiceRepository invoiceRepository;

  @Override
  public int save(Invoice invoice) {
    return invoiceRepository.save(invoice).getId();
  }

  @Override
  public Optional<Invoice> getById(int id) {
    return invoiceRepository.findById(id);
  }

  @Override
  public List<Invoice> getAll() {
    return StreamSupport.stream(invoiceRepository.findAll().spliterator(),false)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<Invoice> update(int id, Invoice updatedInvoice) {
    Optional<Invoice> invoiceOptional = getById(id);

    if (!invoiceOptional.isPresent()) {
      throw new IllegalArgumentException("Invoice with ID " + id + " does not exist and cannot be updated.");
    }

    Invoice invoice = invoiceOptional.get();
    updatedInvoice.setId(id); // just in case it was not set
    updatedInvoice.getBuyer().setId(invoice.getBuyer().getId());
    updatedInvoice.getSeller().setId(invoice.getSeller().getId());

    invoiceRepository.save(updatedInvoice);

    return invoiceOptional;
  }

  @Override
  public void delete(int id) {
    Optional<Invoice> invoice = getById(id);
    invoice.ifPresent(invoiceRepository::delete);
  }
}
