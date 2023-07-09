package pl.futurecollars.invoicing.db.file

import pl.futurecollars.invoicing.db.AbstractDatabaseTest
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.utils.FileService
import pl.futurecollars.invoicing.service.IdService
import pl.futurecollars.invoicing.utils.JsonService

import static pl.futurecollars.invoicing.TestHelpers.invoice

class FileBasedDatabaseIntegrationTest extends AbstractDatabaseTest {

    def databasePath

    @Override
    Database getDatabase() {
        def idPath = File.createTempFile("idss", ".txt").toPath()
        def idService = new IdService(idPath)

        databasePath = File.createTempFile("invoicesDatabase", ".json").toPath()

        return new FileBasedDatabase(databasePath, idService, new JsonService(), new FileService())
    }

    def "file based database should save invoice correctly to correct File"() {
        given:
        def db = getDatabase()
        def invoice = invoice(5)
        def fileService = new FileService()

        when:
        db.save(invoice)

        then:
        1 == fileService.readLinesFromFile(databasePath).size()
    }

    def "should throw exception on deleting non existing record"() {
        given:
        def db = getDatabase()
        when:
        db.delete(34)
        then:
        thrown(RuntimeException.class)
    }
}
