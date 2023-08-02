package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Invoice {

  @ApiModelProperty(value = "Invoice id", required = true)
  private int id;
  @ApiModelProperty(value = "Invoice creation date", required = true)
  private LocalDate date;
  @ApiModelProperty(value = "Buyer name", required = true)
  private Company buyer;
  @ApiModelProperty(value = "Seller name", required = true)
  private Company seller;
  @ApiModelProperty(value = "Invoice entries", required = true)
  private List<InvoiceEntry> entries;

}
