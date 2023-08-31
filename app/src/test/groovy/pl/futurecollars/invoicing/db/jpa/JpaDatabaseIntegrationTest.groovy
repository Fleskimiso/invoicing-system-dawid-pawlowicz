package pl.futurecollars.invoicing.db.jpa

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.annotation.IfProfileValue
import org.springframework.test.context.ActiveProfiles
import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.sql.jpa.InvoiceRepository
import pl.futurecollars.invoicing.db.sql.jpa.JpaDatabase

@DataJpaTest
@ActiveProfiles("jpa")
class JpaDatabaseIntegrationTest extends AbstractDatabaseTest {

    @Autowired
    private InvoiceRepository invoiceRepository

    @Override
    Database getDatabase() {
        assert invoiceRepository != null
        new JpaDatabase(invoiceRepository)
    }
}
