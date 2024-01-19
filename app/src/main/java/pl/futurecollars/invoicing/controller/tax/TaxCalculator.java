package pl.futurecollars.invoicing.controller.tax;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.futurecollars.invoicing.controller.TaxCalculatorResult;

@CrossOrigin
@RequestMapping(value = "/tax")
@Api(tags = {"tax-controller"})
public interface TaxCalculator {


  @ApiOperation(value = "Get tax related information's (incomes, costs, vat and taxes)")
  @GetMapping(value = "/{id}", produces = {"application/json;charset=UTF-8"})
  ResponseEntity<TaxCalculatorResult> calculateTaxes(@PathVariable int id);

}
