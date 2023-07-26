package pl.futurecollars.invoicing.controller.tax;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.futurecollars.invoicing.controller.TaxCalculatorResult;
import pl.futurecollars.invoicing.model.Company;

@RequestMapping(value = "/tax", produces = {"application/json;charset=UTF-8"})
@Api(tags = {"tax-controller"})
public interface TaxCalculator {

  @ApiOperation(value = "Get tax related information's (incomes, costs, vat and taxes)")
  @PostMapping
  TaxCalculatorResult calculateTaxes(@RequestBody Company company);

}
