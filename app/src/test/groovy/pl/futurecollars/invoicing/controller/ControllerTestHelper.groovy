package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.utils.JsonService
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoicing.TestHelpers.invoice
import static pl.futurecollars.invoicing.TestHelpers.company

class ControllerTestHelper extends Specification {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    protected ResultActions deleteItem(String endpoint, int id) {
        mockMvc.perform(delete("$endpoint/$id"))
    }

    protected List<Invoice> getAllInvoices(String endpoint) {
        return getAll(Invoice[].class,endpoint)
    }

    protected List<Company> getAllCompanies(String endpoint) {
        return getAll(Company[].class,endpoint)
    }

    protected Invoice getInvoiceById(String endpoint, int id) {
        return getById(id,Invoice.class,endpoint)
    }

    protected Company getCompanyById(String endpoint, int id) {
        return getById(id,Company.class,endpoint)
    }

    protected int addItemAndReturnId(String endpoint, String itemAsJson) {
        Integer.valueOf(
                mockMvc.perform(
                        post(endpoint)
                                .content(itemAsJson)
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
            invoice.id = addItemAndReturnId(endpoint, jsonService.toJson(invoice))
            return invoice
        }
    }

    protected List<Company> addMultipleCompanies(String endpoint,int count) {
        (1..count).collect { id ->
            def company = company(id)
            company.id = addItemAndReturnId(endpoint, jsonService.toJson(company))
            return company
        }
    }

    protected String invoiceAsJson(int id) {
        jsonService.toJson(invoice(id))
    }

    protected String companyAsJson(int id) {
        jsonService.toJson(company(id))
    }

    private <T> T getAll(Class<T> clazz, String endpoint) {
        def response = mockMvc.perform(get(endpoint))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        jsonService.toObject(response, clazz)
    }

    private <T> T getById(long id, Class<T> clazz, String endpoint) {
        def invoiceAsString = mockMvc.perform(get("$endpoint/$id"))
                .andExpect(status().isOk())
                .andReturn()
                .response
                .contentAsString

        jsonService.toObject(invoiceAsString, clazz)
    }

    protected TaxCalculatorResult getTaxCalculatorResult(String endpoint, Company company) {
        def taxAsString = mockMvc.perform(
                post(endpoint)
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
