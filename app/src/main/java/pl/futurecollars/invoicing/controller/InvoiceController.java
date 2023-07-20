package pl.futurecollars.invoicing.controller;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.InvoiceService;

@RestController
@AllArgsConstructor
public class InvoiceController implements InvoiceControllerApi {

  private final InvoiceService invoiceService;

  @Override
  public ResponseEntity<List<Invoice>> getAllInvoices() {
    return ResponseEntity.ok(invoiceService.getAll());
  }

  @Override
  public ResponseEntity<Integer> saveInvoice(@RequestBody Invoice invoice) {
    return ResponseEntity.status(HttpStatus.CREATED).body(invoiceService.save(invoice));
  }

  @Override
  public ResponseEntity<Invoice> getInvoice(@PathVariable int id) {
    return invoiceService.getById(id)
        .map(invoice -> ResponseEntity.ok().body(invoice))
        .orElse(ResponseEntity.notFound().build());
  }

  @Override
  public ResponseEntity<?> deleteInvoice(@PathVariable int id) {
    try {
      invoiceService.delete(id);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (RuntimeException runtimeException) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @Override
  public ResponseEntity<Invoice> updateInvoice(@PathVariable int id, @RequestBody Invoice invoice) {
    try {
      Optional<Invoice> updatedInvoice = invoiceService.update(id, invoice);
      return updatedInvoice
          .map(optionalInvoice -> ResponseEntity.ok(invoice))
          .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
              .build());
    } catch (RuntimeException exception) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

}
