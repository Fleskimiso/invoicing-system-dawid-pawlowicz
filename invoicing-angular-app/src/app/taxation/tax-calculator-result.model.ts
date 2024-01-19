// tax-calculator-result.model.ts
export interface TaxCalculatorResult {
    income: number;
    costs: number;
    earnings: number;
    pensionInsurance: number;
    earningMinusCost: number;
    incomeTax: number;
    finalTax: number;
    healthInsurance: number;
    incomingVat: number;
    outgoingVat: number;
    vatToReturn: number;
  }
  