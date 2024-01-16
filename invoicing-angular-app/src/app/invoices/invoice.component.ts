// invoice.component.ts
import { Component, OnInit } from '@angular/core';
import { Invoice } from './invoice';
import { InvoiceService } from './invoice.service';
import { Company } from '../companies/company';
import { InvoiceEntry } from './invoice-entry.model';
import { Car } from './car.model';
import { CompanyService } from '../companies/CompanyService';

@Component({
  selector: 'app-invoices',
  templateUrl: './invoice.component.html',
  styleUrls: ['./invoice.component.css']
})
export class InvoiceComponent implements OnInit {
  invoices: Invoice[] = [];
  companies: Company[] = []
  newInvoice: Invoice = new Invoice(0, '', new Date(), new Company(0, '', '', '', 0, 0), new Company(0, '', '', '', 0, 0), []);
  vatRates: number[] = [0, 5, 7, 8, 21];

  constructor(private invoiceService: InvoiceService, private companiesService: CompanyService) {}

  ngOnInit(): void {
    this.invoiceService.getInvoices().subscribe(invoices => {
      this.invoices = invoices;
    });
    this.companiesService.getCompanies()
    .subscribe(companies =>{
      this.companies = companies
    });
  }

  addInvoice() {
    this.invoiceService.addInvoice(this.newInvoice).subscribe(id => {
      this.newInvoice.id = id;
      this.invoices.push(this.newInvoice);
      this.newInvoice = new Invoice(0, '', new Date(), new Company(0, '', '', '', 0, 0), new Company(0, '', '', '', 0, 0), []);
    });
  }

  deleteInvoice(invoiceToDelete: Invoice) {
    this.invoiceService.deleteInvoice(invoiceToDelete.id).subscribe(() => {
      this.invoices = this.invoices.filter(invoice => invoice !== invoiceToDelete);
    });
  }

  addCarForEntry(index: number) {
    if (!this.newInvoice.entries[index].depreciationCosts) {
      this.newInvoice.entries[index].depreciationCosts = new Car(0, '', false);
    }
  }

  onValueChange(fieldName: string, entry: InvoiceEntry) {
    switch (fieldName) {
      case 'quantity':
      case 'price':
      case 'vatRate':
        this.recomputeVatValue(entry);
        break;
    }
  }

  recomputeVatValue(entry: InvoiceEntry) {
    // Recompute VAT value based on your formula
    entry.vatValue = (entry.vatRate / 100) * entry.quantity * entry.price;
  }

  addEntry() {
    // Create a new entry with default values
  const newEntry = new InvoiceEntry(0, "", 0, 0, 0, 0, null);

  // Update the newInvoice object
  this.newInvoice = {
    ...this.newInvoice,
    entries: [...this.newInvoice.entries, newEntry],
  };
}

  deleteEntry(index: number) {
    if (this.newInvoice.entries && index >= 0 && index < this.newInvoice.entries.length) {
      this.newInvoice.entries.splice(index, 1);
    }
  }
}
