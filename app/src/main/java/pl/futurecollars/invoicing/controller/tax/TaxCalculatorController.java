package pl.futurecollars.invoicing.controller.tax;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.controller.TaxCalculatorResult;
import pl.futurecollars.invoicing.service.TaxCalculatorService;

@Slf4j
@RestController
@AllArgsConstructor
public class TaxCalculatorController implements TaxCalculator {

  private final TaxCalculatorService taxCalculatorService;

  @Override
  public ResponseEntity<TaxCalculatorResult> calculateTaxes(@PathVariable int id) {
    return ResponseEntity.ok(taxCalculatorService.calculateTaxes(id));
  }

}
