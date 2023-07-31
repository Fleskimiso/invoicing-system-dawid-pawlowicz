package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.TaxCalculatorResult
import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Unroll
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
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
        def taxCalculatorResponse = getTaxCalculatorResult(TAX_ENDPOINT, "997")

        then:
        taxCalculatorResponse.income ==0
        taxCalculatorResponse.costs==0;
        taxCalculatorResponse.earnings==0;
        taxCalculatorResponse.incomeTax==0;
        taxCalculatorResponse.pensionInsurance==0;
        taxCalculatorResponse.healthInsurance==0;
        taxCalculatorResponse.incomingVat==0;
        taxCalculatorResponse.outgoingVat==0;
        taxCalculatorResponse.vatToReturn ==0;
    }

    def "should calculate from invoice"() {
        given:
        addMultipleInvoices(INVOICE_ENDPOINT,10)

        when:
        def taxCalculatorResponse = getTaxCalculatorResult(TAX_ENDPOINT, "10101010101010101010")

        then:
        taxCalculatorResponse.income == 8000
        taxCalculatorResponse.costs == 8000
        taxCalculatorResponse.pensionInsurance == 2
        taxCalculatorResponse.incomingVat == 168.0
        taxCalculatorResponse.earnings == 0
        taxCalculatorResponse.incomingVat == 1680.0
        taxCalculatorResponse.outgoingVat == 1680.0
        taxCalculatorResponse.vatToReturn == 0
    }


    def "Tax when we used car"() {
        given:
        def inv = carInvoice()
        addInvoiceAndReturnIdInvoice(carInvoice())

        when:
        def taxCalculatorResponse = calc(inv.getSeller())

        then: "seller"
        taxCalculatorResponse.income == 1676.98;
        taxCalculatorResponse.costs == 1600.0;
        taxCalculatorResponse.earnings == 76.98;
        taxCalculatorResponse.pensionInsurance == 4;
        taxCalculatorResponse.earningMinusCost == 73;
        taxCalculatorResponse.incomeTax == 14.6262;
        taxCalculatorResponse.finalTax == -143;
        taxCalculatorResponse.healthInsurance == 158;
        taxCalculatorResponse.incomingVat == 389.29;
        taxCalculatorResponse.outgoingVat == 336.0;
        taxCalculatorResponse.vatToReturn == 53.29;

        when:
        taxCalculatorResponse = calc(inv.getBuyer())

        then: "buyer"
        taxCalculatorResponse.income == 3200;
        taxCalculatorResponse.costs == 3303.63;
        taxCalculatorResponse.earnings == -103.63;
        taxCalculatorResponse.pensionInsurance == 8;
        taxCalculatorResponse.earningMinusCost == -112;
        taxCalculatorResponse.incomeTax == -19.6897;
        taxCalculatorResponse.finalTax == -336;
        taxCalculatorResponse.healthInsurance == 317;
        taxCalculatorResponse.incomingVat == 672.0;
        taxCalculatorResponse.outgoingVat == 698.64;
        taxCalculatorResponse.vatToReturn == -26.64;
    }




    TaxCalculatorResult calc(Company company) {
        def taxAsString = mockMvc.perform(
                post("$ENDPOINT")
                .content(jsonService.toJson(company))
                .contentType(MediaType.APPLICATION_JSON)
        )
                    .andExpect(status().isOk())
                    .andReturn()
                    .response
                    .contentAsString

        jsonService.toObject(taxAsString, TaxCalculatorResult)
    }
}