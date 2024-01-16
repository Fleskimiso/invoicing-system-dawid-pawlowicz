// invoice.ts
import { Company } from '../companies/company';
import { InvoiceEntry } from './invoice-entry.model';

export class Invoice {
  public editMode = false;
  public editedInvoice: Invoice | null = null;

  constructor(
    public id: number,
    public number: string,
    public date: Date ,
    public buyer: Company ,
    public seller: Company ,
    public entries: InvoiceEntry[] 
  ) {}
}

