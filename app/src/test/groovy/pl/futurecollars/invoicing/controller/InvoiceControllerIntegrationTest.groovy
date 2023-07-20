package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.utils.JsonService

import java.time.LocalDate

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoicing.TestHelpers.invoice

@SpringBootTest
@AutoConfigureMockMvc
class InvoiceControllerIntegrationTest extends HelperController {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    private final static String ENDPOINT = "/invoices"

    def setup() {
        getAllInvoices().each { invoice -> deleteInvoice(invoice.id) }
    }

    def "empty array is returned when no invoices were added"() {
        expect:
        getAllInvoices() == []
    }

    def "adding invoices returns sequential id"() {
        given:
        def invoiceAsJson = invoiceAsJson(1)

        expect:
        def firstId = addInvoiceAndReturnId(invoiceAsJson)
        addInvoiceAndReturnId(invoiceAsJson) == firstId + 1
        addInvoiceAndReturnId(invoiceAsJson) == firstId + 2
    }

    def "all invoices are returned when getting all invoices"() {
        given:
        def numberOfInvoices = 5
        def expectedInvoices = addMultipleInvoices(numberOfInvoices)

        when:
        def invoices = getAllInvoices()

        then:
        invoices.size() == numberOfInvoices
        invoices == expectedInvoices
    }

    def "correct invoice is returned when getting by id"() {
        given:
        def expectedInvoices = addMultipleInvoices(5)
        def expectedInvoice = expectedInvoices.get(2)

        when:
        def invoice = getInvoiceById(expectedInvoice.getId())

        then:
        invoice == expectedInvoice
    }

    def "should return 404 status when invoice is not found"() {
        given:
        addMultipleInvoices(4)

        expect:
        mockMvc.perform(
                get("$ENDPOINT/123")
        )
                .andExpect(status().isNotFound())
    }

    def "should return 404 status when deleting nonexistent invoice"() {
        given:
        addMultipleInvoices(4)

        expect:
        mockMvc.perform(
                delete("$ENDPOINT/34")
        )
                .andExpect(status().isNotFound())
    }

    def "should return 404 status when updating nonexistent invoice"() {
        given:
        addMultipleInvoices(5)

        expect:
        mockMvc.perform(
                put("$ENDPOINT/45")
                        .content(invoiceAsJson(1))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isNotFound())
    }

    def "invoice can be modified"() {
        given:
        def id = addInvoiceAndReturnId(invoiceAsJson(44))
        def updatedInvoice = invoice(123)
        updatedInvoice.id = id
        updatedInvoice.date = LocalDate.now()

        expect:
        mockMvc.perform(
                put("$ENDPOINT/$id")
                        .content(jsonService.toJson(updatedInvoice))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())

        getInvoiceById(id) == updatedInvoice
    }

    def "invoice can be deleted"() {
        given:
        def invoices = addMultipleInvoices(5)

        expect:
        invoices.each { invoice -> deleteInvoice(invoice.getId()) }
        getAllInvoices().size() == 0
    }
}
