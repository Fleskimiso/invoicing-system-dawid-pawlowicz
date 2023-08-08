package pl.futurecollars.invoicing.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
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
public class Company {

  @Id
  @JsonIgnore
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @ApiModelProperty(value = "Company id", required = true, example = "1")
  private int id;
  @ApiModelProperty(value = "Tax id number", required = true, example = "1234567891")
  private String taxIdentificationNumber;
  @ApiModelProperty(value = "Company address", required = true, example = "ul.Balladyny 34/3, 56-347 Parczew")
  private String address;
  @ApiModelProperty(value = "Company name", required = true, example = "XGHM company")
  private String name;
  @ApiModelProperty(value = "Amount of health insurance", required = true, example = "221.10")
  private BigDecimal healthInsurance;
  @ApiModelProperty(value = "Amount of pension insurance", required = true, example = "22.98")
  private BigDecimal pensionInsurance;

}
