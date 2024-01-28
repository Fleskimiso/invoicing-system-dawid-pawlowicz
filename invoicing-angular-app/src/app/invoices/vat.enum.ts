// vat.enum.ts
export enum Vat {
  VAT_21 = "VAT_21",
  VAT_8 = "VAT_8",
  VAT_7 = "VAT_7",
  VAT_5 = "VAT_5",
  VAT_0 = "VAT_0",
  VAT_ZW = "VAT_0",

}

export function getVatRateValue(vatRate: Vat): number {
  switch (vatRate) {
    case Vat.VAT_0:
      return 0;
    case Vat.VAT_5:
      return 5;
    case Vat.VAT_7:
      return 7
    case Vat.VAT_8:
      return 8
    case Vat.VAT_21:
      return 21
    default:
      return 0;
  }
}

export function getVatRateFromString(vatRate: string) {
  switch (vatRate) {
    case "VAT_21":
      return Vat.VAT_21;
    case "VAT_8":
      return Vat.VAT_8;
    case "VAT_7":
      return Vat.VAT_7;
    case "VAT_5":
      return Vat.VAT_5;
    case "VAT_0":
      return Vat.VAT_0;
    default:
      return Vat.VAT_ZW;
  }
}

