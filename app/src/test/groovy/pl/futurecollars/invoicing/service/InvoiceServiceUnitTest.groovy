package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.db.Database
import spock.lang.Specification

import static pl.futurecollars.invoicing.TestHelpers.invoice

class InvoiceServiceUnitTest extends Specification {

    private InvoiceService service
    private Database database

    def setup() {
        database = Mock()
        service = new InvoiceService(database)
    }

    def "save should invoke database save method"() {
        given:
        def invoice = invoice(1)
        when:
        service.save(invoice)
        then:
        1 * database.save(invoice)
    }

    def "delete should invoke database delete method"() {
        given:
        def invoiceId = 4
        when:
        service.delete(invoiceId)
        then:
        1 * database.delete(invoiceId)
    }

    def "getById should invoke database getById method"() {
        given:
        def invoiceId = 5
        when:
        service.getById(invoiceId)
        then:
        1 * database.getById(invoiceId)
    }

    def "getAll should invoke database getAll method"() {
        when:
        service.getAll()
        then:
        1 * database.getAll()
    }

    def "update should invoke database update method"() {
        given:
        def invoice = invoice(1)
        when:
        service.update(invoice.getId(), invoice)
        then:
        1 * database.update(invoice.getId(), invoice)
    }

}
