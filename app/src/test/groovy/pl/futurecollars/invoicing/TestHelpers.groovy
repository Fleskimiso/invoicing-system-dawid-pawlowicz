package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat

import java.time.LocalDate

class TestHelpers {

    static company(int id) {
        new Company(("$id").repeat(10),
                "ul. Przemysłowa 25/$id 11-123 Zamość, Polska",
                "Some Industry $id Sp. z o.o")
    }

    static product(int id) {
        new InvoiceEntry("Graphics Card $id", BigDecimal.valueOf(id * 400), BigDecimal.valueOf(id * 400 * 0.21), Vat.VAT_21)
    }

    static invoice(int id) {
        new Invoice(LocalDate.now(), company(id), company(id), List.of(product(id)))
    }
}
