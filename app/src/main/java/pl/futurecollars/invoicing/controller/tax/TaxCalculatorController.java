package pl.futurecollars.invoicing.controller.tax;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.controller.TaxCalculatorResult;
import pl.futurecollars.invoicing.service.TaxCalculatorService;

@RestController
@AllArgsConstructor
public class TaxCalculatorController implements TaxCalculator {

  private final TaxCalculatorService taxCalculatorService;

  @Override
  public TaxCalculatorResult calculateTaxes(@PathVariable String taxIdentificationNumber) {
    return taxCalculatorService.calculateTaxes(taxIdentificationNumber);
  }

}
