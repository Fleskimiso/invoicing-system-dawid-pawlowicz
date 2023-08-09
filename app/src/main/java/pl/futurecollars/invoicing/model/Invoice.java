package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Invoice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ApiModelProperty(value = "Invoice id", required = true)
  private int id;
  @ApiModelProperty(value = "Invoice creation date", required = true)
  private LocalDate date;
  @ApiModelProperty(value = "Invoice number", required = true, example = "2002/01/01/0000209")
  private String number;
  @OneToOne(cascade = CascadeType.ALL)
  @ApiModelProperty(value = "Buyer name", required = true)
  private Company buyer;
  @OneToOne(cascade = CascadeType.ALL)
  @ApiModelProperty(value = "Seller name", required = true)
  private Company seller;
  @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
  @ApiModelProperty(value = "Invoice entries", required = true)
  private List<InvoiceEntry> entries;

}
