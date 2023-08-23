package pl.futurecollars.invoicing.controller.company;

import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.service.CompanyService;

@RestController
@AllArgsConstructor
public class CompanyController implements CompanyApi {

  private final CompanyService companyService;

  @Override
  public List<Company> getAll() {
    return companyService.getAll();
  }

  @Override
  public int add(@RequestBody Company company) {
    return companyService.save(company);
  }

  @Override
  public ResponseEntity<Company> getById(@PathVariable int id) {
    return companyService.getById(id)
        .map(company -> ResponseEntity.ok().body(company))
        .orElse(ResponseEntity.notFound().build());
  }

  @Override
  public ResponseEntity<?> deleteById(@PathVariable int id) {
    try {
      companyService.delete(id);
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } catch (RuntimeException runtimeException) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @Override
  public ResponseEntity<?> update(@PathVariable int id, @RequestBody Company company) {
    try {
      Optional<Company> updatedCompany = companyService.update(id, company);
      return updatedCompany
          .map(optionalCompany -> ResponseEntity.ok(company))
          .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
              .build());
    } catch (RuntimeException exception) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

}
