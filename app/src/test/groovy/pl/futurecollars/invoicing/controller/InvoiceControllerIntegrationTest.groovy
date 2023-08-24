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
class InvoiceControllerIntegrationTest extends ControllerTestHelper {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    private final static String ENDPOINT = "/invoices"

    def setup() {
        getAllInvoices(ENDPOINT).each { invoice -> deleteItem(ENDPOINT, invoice.id) }
    }

    def "empty array is returned when no invoices were added"() {
        expect:
        getAllInvoices(ENDPOINT) == []
    }

    def "adding invoices returns sequential id"() {
        given:
        def invoiceAsJson = invoiceAsJson(1)

        expect:
        def firstId = addItemAndReturnId(ENDPOINT, invoiceAsJson)
        addItemAndReturnId(ENDPOINT, invoiceAsJson) == firstId + 1
        addItemAndReturnId(ENDPOINT, invoiceAsJson) == firstId + 2
    }

    def "all invoices are returned when getting all invoices"() {
        given:
        def numberOfInvoices = 5
        def expectedInvoices = addMultipleInvoices(ENDPOINT, numberOfInvoices)

        when:
        def invoices = getAllInvoices(ENDPOINT)

        then:
        invoices.size() == numberOfInvoices
        invoices.get(0).getNumber() == expectedInvoices.get(0).getNumber()
    }

    def "correct invoice is returned when getting by id"() {
        given:
        def expectedInvoices = addMultipleInvoices(ENDPOINT, 5)
        def expectedInvoice = expectedInvoices.get(2)
        when:
        def invoice = getInvoiceById(ENDPOINT,expectedInvoice.getId())

        then:
        invoice.getNumber() == expectedInvoice.getNumber()
    }

    def "should return 404 status when invoice is not found"() {
        given:
        addMultipleInvoices(ENDPOINT, 4)

        expect:
        mockMvc.perform(
                get("$ENDPOINT/123")
        )
                .andExpect(status().isNotFound())
    }

    def "should return 404 status when deleting nonexistent invoice"() {
        given:
        addMultipleInvoices(ENDPOINT, 4)

        expect:
        mockMvc.perform(
                delete("$ENDPOINT/3489")
        )
                .andExpect(status().isNotFound())
    }

    def "should return 404 status when updating nonexistent invoice"() {
        given:
        addMultipleInvoices(ENDPOINT, 5)

        expect:
        mockMvc.perform(
                put("$ENDPOINT/495")
                        .content(invoiceAsJson(1))
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is4xxClientError())
    }

    def "invoice can be modified"() {
        given:
        def id = addItemAndReturnId(ENDPOINT, invoiceAsJson(44))
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

        getInvoiceById(ENDPOINT, id).getNumber() == updatedInvoice.getNumber()
    }

    def "invoice can be deleted"() {
        given:
        def invoices = addMultipleInvoices(ENDPOINT, 5)

        expect:
        invoices.each { invoice -> deleteItem(ENDPOINT, invoice.getId()) }
        getAllInvoices(ENDPOINT).size() == 0
    }
}
