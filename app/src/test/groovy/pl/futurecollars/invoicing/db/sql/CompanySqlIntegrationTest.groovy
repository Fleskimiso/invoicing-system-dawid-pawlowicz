package pl.futurecollars.invoicing.db.sql

import org.flywaydb.core.Flyway
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import pl.futurecollars.invoicing.db.Database
import pl.futurecollars.invoicing.model.Company
import spock.lang.Specification

import javax.sql.DataSource

import static pl.futurecollars.invoicing.TestHelpers.clearIds
import static pl.futurecollars.invoicing.TestHelpers.company

class CompanySqlIntegrationTest extends Specification {

    private Database<Company> database
    private List<Company> companies = (1..10).collect { company(it) }

    Database getDatabase() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build()
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource)

        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("db/migration")
                .load()
        flyway.clean()
        flyway.migrate()
        println flyway.info()


        def database = new CompanySqlDatabase(jdbcTemplate)
        return database
    }

    def setup() {
        database = getDatabase()
        database.reset();

        assert database.getAll().isEmpty()
    }

    def "should save all the invoices to the database correctly"() {
        given:
        def companiesIds = []

        when:
        for (i in 0..<companies.size()) {
            companiesIds.add(database.save(companies.get(i)))
        }
        then:
        for (i in 0..<companiesIds.size()) {
            assert database.getById(companiesIds.get(i) as int).isPresent()
        }
        cleanup:
        clearIds()
    }

    def "should be able to retrieve all records from database"() {
        when:
        for (i in 0..<companies.size()) {
            database.save(companies.get(i))
        }

        then:
        for (i in 0..<companies.size()) {
            companies.get(i) == database.getAll().get(i)
        }
    }

    def "should return empty optional if the invoice is not present in the database"() {
        expect:
        database.getById(15).isEmpty()
    }

    def "should throw exception on updating non existing record"() {
        when:
        database.update(31, companies.get(0))

        then:
        thrown(IllegalArgumentException)
    }

    def "should update invoice correctly"() {
        when:
        int id = database.save(companies.get(0))
        def updatedCompany = database.getById(id).get()
        updatedCompany.setName("A new comapny namew")
        then:
        database.update(id, updatedCompany)

        expect:
        database.getById(id).get() == updatedCompany
    }

    def "should remove a record from database correctly"() {
        when:
        int id = database.save(companies.get(0))

        then:
        database.delete(id)

        expect:
        database.getById(id).isEmpty()
    }

    def "should throw exception on deleting nonexistent record"(){
        when:
        database.delete(34)

        then:
        thrown(RuntimeException)
    }


}
