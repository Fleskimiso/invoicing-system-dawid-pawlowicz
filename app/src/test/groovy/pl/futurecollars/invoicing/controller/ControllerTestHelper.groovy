package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoicing.TestHelpers.clearIds
import static pl.futurecollars.invoicing.TestHelpers.invoice

class ControllerTestHelper extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    protected ResultActions deleteInvoice(String endpoint, int id) {
        mockMvc.perform(delete("$endpoint/$id"))
    }

    protected List<Invoice> getAllInvoices(String endpoint) {
        def response = mockMvc.perform(get(endpoint))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .getContentAsString()

        return jsonService.toObject(response, Invoice[])
    }

    protected Invoice getInvoiceById(String endpoint, int id) {
        def invoiceAsString = mockMvc.perform(get("$endpoint/$id"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        jsonService.toObject(invoiceAsString, Invoice)
    }

    protected int addInvoiceAndReturnId(String endpoint, String invoiceAsJson) {
        Integer.valueOf(
                mockMvc.perform(
                        post(endpoint)
                                .content(invoiceAsJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                        .andExpect(status().isCreated())
                        .andReturn()
                        .response
                        .contentAsString
        )
    }

    protected List<Invoice> addMultipleInvoices(String endpoint, int count) {
        (1..count).collect { id ->
            def invoice = invoice(id)
            invoice.id = addInvoiceAndReturnId(endpoint, jsonService.toJson(invoice))
            return invoice
        }
    }

    protected String invoiceAsJson(int id) {
        jsonService.toJson(invoice(id))
    }

    protected TaxCalculatorResult getTaxCalculatorResult(String endpoint, String taxIdentificationNumber) {
        print("$endpoint/$taxIdentificationNumber")
        def taxAsString = mockMvc.perform(get("$endpoint/$taxIdentificationNumber"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString
        jsonService.toObject(taxAsString, TaxCalculatorResult)
    }

}
