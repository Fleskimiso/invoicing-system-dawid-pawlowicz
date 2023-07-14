package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InvoiceEntry {

  @ApiModelProperty(value = "Invoice entry description", required = true)
  private String description;
  @ApiModelProperty(value = "Invoice entry price", required = true)
  private BigDecimal price;
  @ApiModelProperty(value = "Invoice entry vat value", required = true)
  private BigDecimal vatValue;
  @ApiModelProperty(value = "Invoice entry vat rate", required = true)
  private Vat vatRate;

}
