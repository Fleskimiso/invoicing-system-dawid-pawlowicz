package pl.futurecollars.invoicing

import pl.futurecollars.invoicing.model.Car
import pl.futurecollars.invoicing.model.Company
import pl.futurecollars.invoicing.model.Invoice
import pl.futurecollars.invoicing.model.InvoiceEntry
import pl.futurecollars.invoicing.model.Vat

import java.time.LocalDate

class TestHelpers {

    static int carId=0
    static int companyId=1
    static int entryId=1

    static company(int id) {
        setCompanyId(companyId+1)
        Company.builder()
                .taxIdentificationNumber(("$id").repeat(10))
                .id(getCompanyId()-1)
                .address("ul. Przemysłowa 25/$id 11-123 Zamość, Polska")
                .name("Some Industry $id Sp. z o.o")
                .healthInsurance(new BigDecimal(10).setScale(2))
                .pensionInsurance(new BigDecimal(20).setScale(2))
                .build()
    }

    static product(int id) {
        setEntryId(getEntryId()+1)
        InvoiceEntry.builder()
                .id(getEntryId()-1)
                .description("Graphics Card $id")
                .price(BigDecimal.valueOf(id * 400).setScale(2))
                .vatValue(BigDecimal.valueOf(id * 400 * 0.21).setScale(2))
                .vatRate(Vat.VAT_21)
                .quantity(1)
                .depreciationCosts(car())
                .build()
    }

    static invoice(int id) {
        Invoice.builder()
                .id(id)
                .date(LocalDate.now())
                .buyer(company(id))
                .seller(company(id))
                .entries(List.of(product(id)))
                .number("2002/01/01/0000209")
                .build()
    }

    static car() {
         Car.builder()
         .id(getCarId())
            .ifPrivateUse(false)
            .registrationNum("ABC 467h")
            .build()
    }

    static clearIds() {
        setCarId(0)
        setCompanyId(1)
        setEntryId(1)
    }

}
