package pl.futurecollars.invoicing.controller;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@Builder
public class TaxCalculatorResult {

  private final BigDecimal income;
  private final BigDecimal costs;
  private final BigDecimal earnings;

  private final BigDecimal incomingVat;
  private final BigDecimal outgoingVat;
  private final BigDecimal vatToReturn;

}
