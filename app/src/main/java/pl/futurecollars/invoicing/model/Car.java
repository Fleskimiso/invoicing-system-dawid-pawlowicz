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
public class Car {


  @ApiModelProperty(value = "Car id", required = true, example = "1")
  private int id;
  @ApiModelProperty(value = "Car registration number", required = true, example = "ABC 467h")
  private String registrationNum;
  @ApiModelProperty(value = "Declaration whether the car was used for private purposes", required = true, example = "false")
  private Boolean ifPrivateUse;
}
