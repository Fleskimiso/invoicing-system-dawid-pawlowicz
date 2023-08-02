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

  @ApiModelProperty(value = "Tax id number", required = true, example = "1234567891")
  private String taxIdentificationNumber;
  @ApiModelProperty(value = "Company address", required = true, example = "ul.Balladyny 34/3, 56-347 Parczew")
  private String address;
  @ApiModelProperty(value = "Company name", required = true, example = "XGHM company")
  private String name;

}
