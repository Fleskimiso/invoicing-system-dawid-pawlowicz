package pl.futurecollars.invoicing.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.service.InvoiceService;

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

  private final InvoiceService invoiceService;

  @Autowired
  public InvoiceController(InvoiceService invoiceService) {
    this.invoiceService = invoiceService;
  }

  @GetMapping()
  public ResponseEntity<List<Invoice>> getAllInvoices() {
    return ResponseEntity.ok(invoiceService.getAll());
  }

  @PostMapping()
  public ResponseEntity<Integer> saveInvoice(@RequestBody Invoice invoice) {
    return ResponseEntity.status(HttpStatus.CREATED).body(invoiceService.save(invoice));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Invoice> getInvoice(@PathVariable int id) {
    return invoiceService.getById(id).map(invoice -> ResponseEntity.ok().body(invoice)).orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteInvoice(@PathVariable int id) {
    invoiceService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PutMapping("/{id}")
  public ResponseEntity<Invoice> updateInvoice(@PathVariable int id, @RequestBody Invoice invoice) {
    Optional<Invoice> updatedInvoice = invoiceService.update(id, invoice);
    return updatedInvoice.map(optionalInvoice -> ResponseEntity.ok(invoice)).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
  }

}
