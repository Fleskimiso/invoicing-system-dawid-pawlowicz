package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat

import java.time.LocalDate

class TestHelpers {

    static company(int id) {
        Company.builder()
                .taxIdentificationNumber(("$id").repeat(10))
                .address("ul. Przemysłowa 25/$id 11-123 Zamość, Polska")
                .name("Some Industry $id Sp. z o.o")
                .build()
    }

    static product(int id) {
        InvoiceEntry.builder()
                .description("Graphics Card $id")
                .price(BigDecimal.valueOf(id * 400))
                .vatValue(BigDecimal.valueOf(id * 400 * 0.21))
                .vatRate(Vat.VAT_21)
                .build()
    }

    static invoice(int id) {
        Invoice.builder()
                .date(LocalDate.now())
                .buyer(company(id))
                .seller(company(id))
                .entries(List.of(product(id)))
                .build()
    }
}
