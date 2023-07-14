package pl.futurecollars.invoicing.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Company {

  @ApiModelProperty(value = "Tax id number", required = true, example = "000-000-00-00")
  private String taxIdentificationNumber;
  @ApiModelProperty(value = "Company address", required = true, example = "ul.***, 00-000 *****")
  private String address;
  @ApiModelProperty(value = "Company name", required = true, example = "InvoiceCreator")
  private String name;

}
