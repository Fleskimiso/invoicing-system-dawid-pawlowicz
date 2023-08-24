package pl.futurecollars.invoicing.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Company;

@Service
public class CompanyService {

  @Qualifier("company")
  private final Database<Company> database;

  public CompanyService(Database<Company> database) {
    this.database = database;
  }

  public int save(Company company) {
    return database.save(company);
  }

  public Optional<Company> getById(int id) {
    return database.getById(id);
  }

  public List<Company> getAll() {
    return database.getAll();
  }

  public Optional<Company> update(int id, Company updatedCompany) {
    updatedCompany.setId(id);
    return database.update(id, updatedCompany);
  }

  public void delete(int id) {
    database.delete(id);
  }

}
