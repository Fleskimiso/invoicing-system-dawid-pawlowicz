package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Unroll
import static pl.futurecollars.invoicing.TestHelpers.carInvoice
import static pl.futurecollars.invoicing.TestHelpers.company


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
        def taxCalculatorResponse = getTaxCalculatorResult(TAX_ENDPOINT, company(30))

        then:
        taxCalculatorResponse.income ==0
        taxCalculatorResponse.costs==0
        taxCalculatorResponse.earnings==0
        taxCalculatorResponse.incomeTax==0
        taxCalculatorResponse.pensionInsurance==30
        taxCalculatorResponse.healthInsurance==1188
        taxCalculatorResponse.incomingVat==0
        taxCalculatorResponse.outgoingVat==0
        taxCalculatorResponse.vatToReturn ==0
    }

    def "should calculate from invoice"() {
        given:
        addMultipleInvoices(INVOICE_ENDPOINT,10)

        when:
        def taxCalculatorResponse = getTaxCalculatorResult(TAX_ENDPOINT, company(10))

        then:
        taxCalculatorResponse.income == 8000
        taxCalculatorResponse.costs == 8000
        taxCalculatorResponse.pensionInsurance == 10
        taxCalculatorResponse.incomeTax == 0.0
        taxCalculatorResponse.earnings == 0
        taxCalculatorResponse.incomingVat == 1680.0
        taxCalculatorResponse.outgoingVat == 1680.0
        taxCalculatorResponse.vatToReturn == 0
    }


    def "Tax when we used car"() {
        given:
        def inv = carInvoice()
        addInvoiceAndReturnId(INVOICE_ENDPOINT,jsonService.toJson(inv))

        when:
        def taxCalculatorResponse = getTaxCalculatorResult(TAX_ENDPOINT,inv.getSeller())

        then: "seller"
        taxCalculatorResponse.income == 3276.98
        taxCalculatorResponse.costs == 3200.0
        taxCalculatorResponse.earnings == 76.98
        taxCalculatorResponse.pensionInsurance == 4
        taxCalculatorResponse.earningMinusCost == 73
        taxCalculatorResponse.incomeTax == 14.6262
        taxCalculatorResponse.finalTax == -143
        taxCalculatorResponse.healthInsurance == 158
        taxCalculatorResponse.incomingVat == 725.29
        taxCalculatorResponse.outgoingVat == 672.0
        taxCalculatorResponse.vatToReturn == 53.29

        when:
        taxCalculatorResponse = getTaxCalculatorResult(TAX_ENDPOINT,inv.getBuyer())

        then: "buyer"
        taxCalculatorResponse.income == 6400
        taxCalculatorResponse.costs == 6503.63
        taxCalculatorResponse.earnings == -103.63
        taxCalculatorResponse.pensionInsurance == 8
        taxCalculatorResponse.earningMinusCost == -112
        taxCalculatorResponse.incomeTax == -19.6897
        taxCalculatorResponse.finalTax == -336
        taxCalculatorResponse.healthInsurance == 317
        taxCalculatorResponse.incomingVat == 1344.0
        taxCalculatorResponse.outgoingVat == 1370.64
        taxCalculatorResponse.vatToReturn == -26.64
    }

}