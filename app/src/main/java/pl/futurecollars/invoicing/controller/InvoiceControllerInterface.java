package pl.futurecollars.invoicing.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.futurecollars.invoicing.model.Invoice;

@RequestMapping("/invoices")
@Api(tags = {"invoice-controller"})
public interface InvoiceControllerInterface {

  @GetMapping(produces = {"application/json;charset=UTF-8"})
  @ApiOperation(value = "Get all invoices")
  ResponseEntity<List<Invoice>> getAllInvoices();

  @PostMapping()
  @ApiOperation(value = "Save invoice")
  ResponseEntity<Integer> saveInvoice(@RequestBody Invoice invoice);

  @GetMapping(value = "/{id}", produces = {"application/json;charset=UTF-8"})
  @ApiOperation(value = "Get invoice by id")
  ResponseEntity<Invoice> getInvoice(@PathVariable int id);

  @DeleteMapping("/{id}")
  @ApiOperation(value = "Delete invoice")
  ResponseEntity<?> deleteInvoice(@PathVariable int id);

  @PutMapping(value = "/{id}", produces = {"application/json;charset=UTF-8"})
  @ApiOperation(value = "Update invoice")
  ResponseEntity<Invoice> updateInvoice(@PathVariable int id, @RequestBody Invoice invoice);
}
