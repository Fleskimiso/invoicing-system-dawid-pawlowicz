package pl.futurecollars.invoicing.db


import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

import static pl.futurecollars.invoicing.TestHelpers.clearIds
import static pl.futurecollars.invoicing.TestHelpers.invoice

abstract class AbstractDatabaseTest extends Specification {

    private Database database = getDatabase()
    private List<Invoice> invoices = (1..10).collect { invoice(it) }

    abstract Database getDatabase()

    def "should save all the invoices to the database correctly"() {
        given:
        def invoicesId = []

        when:
        for (i in 0..<invoices.size()) {
            invoicesId.add(database.save(invoices.get(i)))
        }
        then:
        for (i in 0..<invoicesId.size()) {
            assert database.getById(invoicesId.get(i) as int).isPresent()
        }
        cleanup:
        clearIds()
    }

    def "should be able to retrieve all records from database"() {
        when:
        for (i in 0..<invoices.size()) {
            database.save(invoices.get(i))
        }

        then:
        invoices == database.getAll()
    }

    def "should return empty optional if the invoice is not present in the database"() {
        expect:
        database.getById(10).isEmpty()
    }

    def "should throw exception on updating non existing record"() {
        when:
        database.update(31, invoices.get(0))

        then:
        thrown(IllegalArgumentException)
    }

    def "should update invoice correctly"() {
        when:
        int id = database.save(invoices.get(0))
        def updatedInvoice = database.getById(id).get()
        updatedInvoice.setNumber("test number")
        updatedInvoice.entries.forEach {it.setId(it.getId()+1)}
        then:
        database.update(id, updatedInvoice)

        expect:
        database.getById(id).get() == updatedInvoice
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
