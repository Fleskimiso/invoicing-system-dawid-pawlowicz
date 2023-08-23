package pl.futurecollars.invoicing.db.sql.jpa;

import org.springframework.data.repository.CrudRepository;
import pl.futurecollars.invoicing.model.Invoice;

public interface InvoiceRepository extends CrudRepository<Invoice, Integer> {

}
