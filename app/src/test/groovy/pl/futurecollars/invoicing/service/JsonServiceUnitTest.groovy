package pl.futurecollars.invoicing.service

import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Specification

import static pl.futurecollars.invoicing.TestHelpers.invoice

class JsonServiceUnitTest extends Specification {

    def "should convert object to json and read it back"() {
        given:
        def jsonService = new JsonService()
        def invoice = invoice(5)

        when:
        def invoiceAsString = jsonService.toJson(invoice)

        and:
        def invoiceFromJson = jsonService.toObject(invoiceAsString, Invoice.class)

        then:
        invoice.getNumber() == invoiceFromJson.getNumber()
    }

}
