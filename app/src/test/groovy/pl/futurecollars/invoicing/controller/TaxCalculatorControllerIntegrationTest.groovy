package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.TaxCalculatorResult
import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoicing.TestHelpers.invoice


@SpringBootTest
@AutoConfigureMockMvc
@Unroll
class TaxCalculatorControllerIntegrationTest extends HelperController {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    private final static String ENDPOINT = "/tax"

    def "should return 0 when not found invoice"() {
        given:
        addMultipleInvoices(10)

        when:
        def taxCalculatorResponse = calc("997")

        then:
        taxCalculatorResponse.income == 0
        taxCalculatorResponse.costs == 0
        taxCalculatorResponse.earnings == 0
        taxCalculatorResponse.incomingVat == 0
        taxCalculatorResponse.outgoingVat == 0
        taxCalculatorResponse.vatToReturn == 0
    }

    def "should calculate from invoice"() {
        given:
        addMultipleInvoices(10)

        when:
        def taxCalculatorResponse = calc("10101010101010101010")

        then:
        taxCalculatorResponse.income == 8000
        taxCalculatorResponse.costs == 8000
        taxCalculatorResponse.earnings == 0
        taxCalculatorResponse.incomingVat == 1680.0
        taxCalculatorResponse.outgoingVat == 1680.0
        taxCalculatorResponse.vatToReturn == 0
        }

    TaxCalculatorResult calc(String taxIdentificationNumber) {
        def taxAsString = mockMvc.perform(get("$ENDPOINT/$taxIdentificationNumber"))
                    .andExpect(status().isOk())
                    .andReturn()
                    .response
                    .contentAsString
        jsonService.toObject(taxAsString, TaxCalculatorResult) }
}