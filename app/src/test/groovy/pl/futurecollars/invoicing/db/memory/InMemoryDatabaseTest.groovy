package pl.futurecollars.invoicing.db.memory

import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

import static pl.futurecollars.invoicing.TestHelpers.invoice

class InMemoryDatabaseTest extends Specification {
    private Database database

    private List<Invoice> invoices

    def setup() {
        database = new InMemoryDatabase()
        invoices = (1..10).collect { invoice(it) }
    }

    def "should save all the invoices to the database correctly"() {
        when:
        def invoicesId = invoices.collect { database.save(it) }

        then:
        invoicesId.forEach { invoiceId -> assert database.getById(invoiceId as int).isPresent() }
    }

    def "should be able to retrieve all records from database"() {
        when:
        invoices.forEach { database.save(it) }

        then:
        invoices == database.getAll()
    }

    def "should return empty optional if the invoice is not present in the database"() {
        expect:
        database.getById(10).isEmpty()
    }

    def "should throw exception on updating non existing record"() {
        when:
        database.update(1, invoices.get(0))

        then:
        thrown(IllegalArgumentException)
    }

    def "should update invoice correctly"() {
        when:
        int id = database.save(invoices.get(0))

        then:
        database.update(id, invoices.get(1))

        expect:
        database.getById(id).get() == invoices.get(1)
    }

    def "should remove a record from database correctly"() {
        when:
        int id = database.save(invoices.get(0))

        then:
        database.delete(id)

        expect:
        database.getById(id).isEmpty()
    }
}
