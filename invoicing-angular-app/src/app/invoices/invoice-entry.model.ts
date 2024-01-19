import { Car } from "./car.model";
import { Vat } from "./vat.enum";

// invoice-entry.model.ts
export class InvoiceEntry {
    public id: number;
    public description: string;
    public quantity: number;
    public price: number;
    public vatValue: number;
    public vatRate: string;
    public depreciationCosts: Car | null;
  
    constructor(
      id: number,
      description: string,
      quantity: number,
      price: number,
      vatValue: number,
      vatRate: string,
      depreciationCosts: Car | null
    ) {
      this.id = id;
      this.description = description;
      this.quantity = quantity;
      this.price = price;
      this.vatValue = vatValue;
      this.vatRate = vatRate;
      this.depreciationCosts = depreciationCosts;
    }
  }
  
