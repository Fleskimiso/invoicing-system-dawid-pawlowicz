package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoicing.TestHelpers.invoice

class HelperController extends Specification{

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    private final static String ENDPOINT = "/invoices"

    protected ResultActions deleteInvoice(int id) {
        mockMvc.perform(delete("$ENDPOINT/$id"))
                .andExpect(status().isNoContent())
    }

    protected List<Invoice> getAllInvoices() {
        def response = mockMvc.perform(get(ENDPOINT))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .getContentAsString()

        return jsonService.toObject(response, Invoice[])
    }

    protected Invoice getInvoiceById(int id) {
        def invoiceAsString = mockMvc.perform(get("$ENDPOINT/$id"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        jsonService.toObject(invoiceAsString, Invoice)
    }

    protected int addInvoiceAndReturnId(String invoiceAsJson) {
        Integer.valueOf(
                mockMvc.perform(
                        post(ENDPOINT)
                                .content(invoiceAsJson)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                        .andExpect(status().isCreated())
                        .andReturn()
                        .response
                        .contentAsString
        )
    }

    protected List<Invoice> addMultipleInvoices(int count) {
        (1..count).collect { id ->
            def invoice = invoice(id)
            invoice.id = addInvoiceAndReturnId(jsonService.toJson(invoice))
            return invoice
        }
    }

    protected String invoiceAsJson(int id) {
        jsonService.toJson(invoice(id))
    }

}
