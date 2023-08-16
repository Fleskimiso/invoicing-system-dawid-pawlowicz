package pl.futurecollars.invoicing.db.nosql

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database

@SpringBootTest
@ActiveProfiles("mongo")
class MongoDatabaseIntegrationTest extends AbstractDatabaseTest {

    @Autowired
    private MongoBasedDatabase mongoDatabase

    @Override
    Database getDatabase() {
        assert mongoDatabase != null
        mongoDatabase
    }

}
