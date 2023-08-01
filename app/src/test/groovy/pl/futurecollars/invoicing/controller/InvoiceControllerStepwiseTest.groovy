package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Specification
import spock.lang.Stepwise

import java.time.LocalDate

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoicing.TestHelpers.clearIds
import static pl.futurecollars.invoicing.TestHelpers.invoice

@SpringBootTest
@AutoConfigureMockMvc
@Stepwise
class InvoiceControllerStepwiseTest extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    private Invoice invoice
    private LocalDate updatedDate = LocalDate.now()

    static invoiceId
    private final static String ENDPOINT = "/invoices"

    def setup() {
        clearIds()
        invoice = invoice(1)
    }

    def "empty array is returned when no invoices are added"() {

        when:
        def response = mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .getContentAsString()

        then:
        response == "[]"
    }

    def "should return id of added invoice"() {

        given:
        def invoiceJson = jsonService.toJson(invoice)

        when:
        def response = mockMvc.perform(post(ENDPOINT)
                .content(invoiceJson)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is2xxSuccessful())
                .andReturn()
                .response
                .getContentAsString()
        invoiceId = Integer.valueOf(response)


        then:
        invoiceId > 0
    }

    def "should return correct invoice"() {

        given:
        def originalInvoice = invoice
        originalInvoice.id = invoiceId

        when:
        def response = mockMvc.perform(get("$ENDPOINT/$invoiceId"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        def responseInvoice = jsonService.toObject(response, Invoice)

        then:
        originalInvoice.getNumber() == responseInvoice.getNumber()
    }

    def "invoice can be modified and returned correctly"() {

        given:
        def originalInvoice = invoice
        originalInvoice.id = invoiceId
        originalInvoice.date = updatedDate

        def invoiceJson = jsonService.toJson(originalInvoice)

        when:
        def response = mockMvc.perform(
                put("$ENDPOINT/$invoiceId")
                        .content(invoiceJson)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andReturn()
                .response
                .getContentAsString()

        then:
        invoiceJson == response
    }

    def "should delete invoice"() {
        when:
        mockMvc.perform(delete("$ENDPOINT/$invoiceId"))
                .andExpect(status().isNoContent())

        then:
        def response = mockMvc.perform(get(ENDPOINT))
                .andReturn()
                .response
                .getContentAsString()
        response == "[]"
    }

}
