package pl.futurecollars.invoicing.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.futurecollars.invoicing.db.Database;
import pl.futurecollars.invoicing.model.Car;
import pl.futurecollars.invoicing.model.Company;
import pl.futurecollars.invoicing.model.Invoice;
import pl.futurecollars.invoicing.model.InvoiceEntry;
import pl.futurecollars.invoicing.model.TaxCalculatorResult;

@Service
@AllArgsConstructor
public class TaxCalculatorService {

  private final Database database;

  public BigDecimal income(String taxIdentificationNumber) {
    return database.visit(sellerPredicate(taxIdentificationNumber), InvoiceEntry::getPrice);
  }

  public BigDecimal costs(String taxIdentificationNumber) {
    return database.visit(buyerPredicate(taxIdentificationNumber), this::getIncome);
  }

  public BigDecimal incomingVat(String taxIdentificationNumber) {
    return database.visit(sellerPredicate(taxIdentificationNumber), InvoiceEntry::getVatValue);
  }

  public BigDecimal outgoingVat(String taxIdentificationNumber) {
    return database.visit(buyerPredicate(taxIdentificationNumber), this::vatValue);
  }

  public BigDecimal getEarnings(String taxIdentificationNumber) {
    return income(taxIdentificationNumber).subtract(costs(taxIdentificationNumber));
  }

  public BigDecimal getVatToReturn(String taxIdentificationNumber) {
    return incomingVat(taxIdentificationNumber).subtract(outgoingVat(taxIdentificationNumber));
  }

  public TaxCalculatorResult calculateTaxes(Company company) {
    String taxIdentificationNumber = company.getTaxIdentificationNumber();
    BigDecimal earnings = getEarnings(taxIdentificationNumber);
    BigDecimal earningsMinusPensionInsurance = earnings.subtract(company.getPensionInsurance());
    BigDecimal earningsMinusRound = earningsMinusPensionInsurance.setScale(0, RoundingMode.HALF_DOWN);
    BigDecimal incomeTax = earnings.multiply(BigDecimal.valueOf(19, 2));
    BigDecimal healthInsurance = company.getHealthInsurance().multiply(BigDecimal.valueOf(775)).divide(BigDecimal.valueOf(900), RoundingMode.HALF_UP);
    BigDecimal incomeTaxMinusHealthInsurance = incomeTax.subtract(healthInsurance);

    return TaxCalculatorResult.builder()
        .income(income(taxIdentificationNumber))
        .costs(costs(taxIdentificationNumber))
        .earnings(earnings)
        .pensionInsurance(company.getPensionInsurance())
        .earningMinusCost(earningsMinusRound)
        .healthInsurance(healthInsurance)
        .incomeTax(incomeTax)
        .finalTax(incomeTaxMinusHealthInsurance.setScale(0, RoundingMode.DOWN))
        .incomingVat(incomingVat(taxIdentificationNumber))
        .outgoingVat(outgoingVat(taxIdentificationNumber))
        .vatToReturn(getVatToReturn(taxIdentificationNumber))
        .build();
  }

  private Predicate<Invoice> sellerPredicate(String taxIdentificationNumber) {
    return invoice -> invoice.getSeller().getTaxIdentificationNumber().equals(taxIdentificationNumber);
  }

  private Predicate<Invoice> buyerPredicate(String taxIdentificationNumber) {
    return invoice -> invoice.getBuyer().getTaxIdentificationNumber().equals(taxIdentificationNumber);
  }

  private BigDecimal vatValue(InvoiceEntry invoiceEntry) {
    return Optional.ofNullable(invoiceEntry.getDepreciationCosts())
        .map(Car::getIfPrivateUse)
        .map(personalCarUsage -> personalCarUsage ? BigDecimal.valueOf(5, 1) : BigDecimal.ONE)
        .map(proportion -> invoiceEntry.getVatValue().multiply(proportion))
        .map(value -> value.setScale(2, RoundingMode.FLOOR))
        .orElse(invoiceEntry.getVatValue());
  }

  private BigDecimal getIncome(InvoiceEntry invoiceEntry) {
    return invoiceEntry.getPrice()
        .add(invoiceEntry.getVatValue())
        .subtract(vatValue(invoiceEntry));
  }

}
