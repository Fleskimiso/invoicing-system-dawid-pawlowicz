package pl.futurecollars.invoicing.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import pl.futurecollars.invoicing.utils.JsonService


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static pl.futurecollars.invoicing.TestHelpers.company

@WithMockUser
@SpringBootTest
@AutoConfigureMockMvc
class CompanyControllerIntegrationTest extends ControllerTestHelper {

    @Autowired
    private MockMvc mockMvc

    @Autowired
    private JsonService jsonService

    private final static String ENDPOINT = "/companies"

    def setup() {
        getAllCompanies(ENDPOINT).each { invoice -> deleteItem(ENDPOINT, invoice.id) }
    }

    def "empty array is returned when no companies were added"() {
        expect:
        getAllCompanies(ENDPOINT) == []
    }

    def "adding companies returns sequential id"() {
        given:
        def companyAsJson = companyAsJson(1)

        expect:
        def firstId = addItemAndReturnId(ENDPOINT, companyAsJson)
        addItemAndReturnId(ENDPOINT, companyAsJson) == firstId + 1
        addItemAndReturnId(ENDPOINT, companyAsJson) == firstId + 2
    }

    def "all companies are returned when getting all companies"() {
        given:
        def numberOfCompanies = 5
        def expectedCompanies = addMultipleCompanies(ENDPOINT, numberOfCompanies)

        when:
        def companies = getAllCompanies(ENDPOINT)

        then:
        companies.size() == numberOfCompanies
        companies.get(0) == expectedCompanies.get(0)
    }

    def "correct company is returned when getting by id"() {
        given:
        def expectedCompanies = addMultipleCompanies(ENDPOINT, 5)
        def expectedCompany = expectedCompanies.get(2)
        when:
        def company = getCompanyById(ENDPOINT,expectedCompany.getId())

        then:
        company == expectedCompany
    }

    def "should return 404 status when company is not found"() {
        given:
        addMultipleCompanies(ENDPOINT, 4)

        expect:
        mockMvc.perform(
                get("$ENDPOINT/123")
                .with(csrf())
        )
                .andExpect(status().isNotFound())
    }

    def "should return 404 status when deleting nonexistent company"() {
        given:
        addMultipleCompanies(ENDPOINT, 4)

        expect:
        mockMvc.perform(
                delete("$ENDPOINT/3489")
                .with(csrf())
        )
                .andExpect(status().isNotFound())
    }

    def "should return 404 status when updating nonexistent company"() {
        given:
        addMultipleCompanies(ENDPOINT, 5)

        expect:
        mockMvc.perform(
                put("$ENDPOINT/495")
                        .content(invoiceAsJson(1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with (csrf())
        )
                .andExpect(status().is4xxClientError())
    }

    def "company can be modified"() {
        given:
        def id = addItemAndReturnId(ENDPOINT, invoiceAsJson(44))
        def updatedCompany = company(123)
        updatedCompany.id = id
        updatedCompany.name = "A new company name"

        expect:
        mockMvc.perform(
                put("$ENDPOINT/$id")
                        .content(jsonService.toJson(updatedCompany))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf())
        )
                .andExpect(status().isOk())

        getCompanyById(ENDPOINT, id) == updatedCompany
    }

    def "company can be deleted"() {
        given:
        def companies = addMultipleCompanies(ENDPOINT, 5)

        expect:
        companies.each { company -> deleteItem(ENDPOINT, company.getId()) }
        getAllCompanies(ENDPOINT).size() == 0
    }
}
