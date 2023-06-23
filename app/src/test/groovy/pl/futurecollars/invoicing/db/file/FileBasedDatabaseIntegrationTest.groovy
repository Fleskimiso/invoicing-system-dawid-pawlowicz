package pl.futurecollars.invoicing.db.file

import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.FileBasedDatabase
import pl.futurecollars.invoicing.service.FileService
import pl.futurecollars.invoicing.service.IdService
import pl.futurecollars.invoicing.service.JsonService

import static pl.futurecollars.invoicing.TestHelpers.invoice

class FileBasedDatabaseIntegrationTest extends AbstractDatabaseTest {

    def databasePath

    @Override
    Database getDatabase() {
        def idPath = File.createTempFile("idss", ".txt").toPath()
        def idService = new IdService(idPath)

        databasePath = File.createTempFile("invoicesDatabase", ".json").toPath()

        return new FileBasedDatabase(databasePath, idService, new JsonService())
    }

    def "file based database should save invoice correctly to correct File"() {
        given:
        def db = getDatabase()
        def invoice = invoice(5)

        when:
        db.save(invoice)

        then:
        1 == FileService.readLinesFromFile(databasePath).size()
    }
}
