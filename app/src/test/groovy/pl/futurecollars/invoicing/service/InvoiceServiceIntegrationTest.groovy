package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.db.memory.InMemoryDatabase
import pl.futurecollars.invoicing.model.Invoice
import spock.lang.Specification

import static  pl.futurecollars.invoicing.TestHelpers.invoice

class InvoiceServiceIntegrationTest extends Specification {

    private InvoiceService service
    private List<Invoice>  invoices

    def setup() {
        Database database = new InMemoryDatabase()
        service = new InvoiceService(database)

        invoices = (1..10).collect{invoice(it)}
    }

    def "should save all the invoices to the database service correctly"() {
        when:
        def invoicesId = invoices.collect { service.save(it) }

        then:
        invoicesId.forEach { invoiceId -> assert service.getById(invoiceId as int).isPresent() }
    }

    def "should be able to retrieve all records from database"() {
        when:
        invoices.forEach { service.save(it) }

        then:
        invoices == service.getAll()
    }

    def "should return empty optional if the invoice is not present in the database"() {
        expect:
        service.getById(10).isEmpty()
    }

    def "should throw exception on updating non existing record"() {
        when:
        service.update(1, invoices.get(0))

        then:
        thrown(IllegalArgumentException)
    }

    def "should update invoice correctly"() {
        when:
        int id = service.save(invoices.get(0))

        then:
        service.update(id, invoices.get(1))

        expect:
        service.getById(id).get() == invoices.get(1)
    }

    def "should remove a record from database service correctly"() {
        when:
        int id = service.save(invoices.get(0))

        then:
        service.delete(id)

        expect:
        service.getById(id).isEmpty()
    }
}
