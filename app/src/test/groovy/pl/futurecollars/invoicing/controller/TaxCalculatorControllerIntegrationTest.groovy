package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Unroll

@SpringBootTest
@AutoConfigureMockMvc
@Unroll
class TaxCalculatorControllerIntegrationTest extends ControllerTestHelper {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    private final static String TAX_ENDPOINT = "/tax"
    private final static String INVOICE_ENDPOINT = "/invoices"

    def "should return 0 when not found invoice"() {
        given:
        addMultipleInvoices(INVOICE_ENDPOINT,10)

        when:
        def taxCalculatorResponse = getTaxCalculatorResult(TAX_ENDPOINT, "997")

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
        addMultipleInvoices(INVOICE_ENDPOINT,10)

        when:
        def taxCalculatorResponse = getTaxCalculatorResult(TAX_ENDPOINT, "10101010101010101010")

        then:
        taxCalculatorResponse.income == 8000
        taxCalculatorResponse.costs == 8000
        taxCalculatorResponse.earnings == 0
        taxCalculatorResponse.incomingVat == 1680.0
        taxCalculatorResponse.outgoingVat == 1680.0
        taxCalculatorResponse.vatToReturn == 0
    }

}