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
        taxCalculatorResponse.pensionInsurance==20
        taxCalculatorResponse.healthInsurance==8.61
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
        taxCalculatorResponse.pensionInsurance == 20
        taxCalculatorResponse.incomeTax == 0.0
        taxCalculatorResponse.earnings == 0
        taxCalculatorResponse.incomingVat == 1680.0
        taxCalculatorResponse.outgoingVat == 1680.0
        taxCalculatorResponse.vatToReturn == 0
    }


    def "Tax when we used car"() {
        given:
        def inv = carInvoice()
        addItemAndReturnId(INVOICE_ENDPOINT,jsonService.toJson(inv))

        when:
        def taxCalculatorResponse = getTaxCalculatorResult(TAX_ENDPOINT,inv.getSeller())

        then: "seller"
        taxCalculatorResponse.income == 3600.0
        taxCalculatorResponse.costs == 3200.0
        taxCalculatorResponse.earnings == 400.0
        taxCalculatorResponse.pensionInsurance == 20
        taxCalculatorResponse.earningMinusCost == 380
        taxCalculatorResponse.incomeTax == 76.0
        taxCalculatorResponse.finalTax == 67
        taxCalculatorResponse.healthInsurance == 8.61
        taxCalculatorResponse.incomingVat == 756.0
        taxCalculatorResponse.outgoingVat == 672.0
        taxCalculatorResponse.vatToReturn == 84

        when:
        taxCalculatorResponse = getTaxCalculatorResult(TAX_ENDPOINT,inv.getBuyer())

        then: "buyer"
        taxCalculatorResponse.income == 6400
        taxCalculatorResponse.costs == 6800
        taxCalculatorResponse.earnings == -400
        taxCalculatorResponse.pensionInsurance == 20
        taxCalculatorResponse.earningMinusCost == -420
        taxCalculatorResponse.incomeTax == -76
        taxCalculatorResponse.finalTax == -84
        taxCalculatorResponse.healthInsurance == 8.61
        taxCalculatorResponse.incomingVat == 1344.0
        taxCalculatorResponse.outgoingVat == 1428.0
        taxCalculatorResponse.vatToReturn == -84.0
    }
}