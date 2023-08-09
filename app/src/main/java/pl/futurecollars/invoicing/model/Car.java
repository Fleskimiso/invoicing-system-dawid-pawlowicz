package pl.futurecollars.invoicing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Car {

  @Id
  @JsonIgnore
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ApiModelProperty(value = "Car id", required = true, example = "1")
  private int id;
  @ApiModelProperty(value = "Car registration number", required = true, example = "ABC 467h")
  private String registrationNummber;
  @ApiModelProperty(value = "Declaration whether the car was used for private purposes", required = true, example = "false")
  private Boolean ifPrivateUse;
}
