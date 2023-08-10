package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class InvoiceEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ApiModelProperty(value = "Invoice entry id", required = true, example = "1")
  private int id;
  @ApiModelProperty(value = "Invoice entry description", required = true, example = "Laptop acer aspire 5")
  private String description;
  @ApiModelProperty(value = "Quantity", required = true, example = "1")
  private int quantity;
  @ApiModelProperty(value = "Invoice entry price", required = true, example = "2999.99")
  private BigDecimal price;
  @ApiModelProperty(value = "Invoice entry vat value", required = true, example = "600")
  private BigDecimal vatValue;
  @ApiModelProperty(value = "Invoice entry vat rate", required = true, example = "VAT_21")
  private Vat vatRate;
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "id")
  @ApiModelProperty(value = "Optional expenses for the car")
  private Car depreciationCosts;
}
