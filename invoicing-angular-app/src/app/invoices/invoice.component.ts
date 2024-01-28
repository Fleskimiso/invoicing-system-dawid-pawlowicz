// invoice.component.ts
import { Component, NgZone, OnInit } from '@angular/core';
import { Invoice } from './invoice';
import { InvoiceService } from './invoice.service';
import { Company } from '../companies/company';
import { InvoiceEntry } from './invoice-entry.model';
import { Car } from './car.model';
import { CompanyService } from '../companies/CompanyService';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { Vat, getVatRateFromString, getVatRateValue} from './vat.enum';


@Component({
  selector: 'app-invoices',
  templateUrl: './invoice.component.html',
  styleUrls: ['./invoice.component.css']
})
export class InvoiceComponent implements OnInit {
  invoices: Invoice[] = [];
  companies: Company[] = []
  newInvoice: Invoice = new Invoice(0, '', new Date(), new Company(0, '', '', '', 0, 0), new Company(0, '', '', '', 0, 0), []);
  vatRates: Vat[] = [Vat.VAT_0, Vat.VAT_5, Vat.VAT_7, Vat.VAT_8, Vat.VAT_21, Vat.VAT_ZW];

  editingInvoice: Invoice | null = null;
  invoiceForm: FormGroup;
  formEntries: FormGroup;

  constructor(private invoiceService: InvoiceService, private companiesService: CompanyService,
    private fb: FormBuilder,private ngZone: NgZone ) {

    this.invoiceForm = this.fb.group({
      number: [''],
      buyer: [null],
      seller: [null],
      buyerName: [''],
      sellerName: [''],
      date: [''],
    });
    this.formEntries = this.fb.group({
      formArray: this.fb.array([])
    })

  }

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

  addCarForEditedEntry(index: number): void {
    const formArray = this.formEntries.get('formArray') as FormArray;

    console.log(index);
    
  
    if (formArray && formArray.controls.length > index) {
      const entryFormGroup = formArray.at(index) as FormGroup;
      const depreciationCostsFormGroup = entryFormGroup.get('depreciationCosts') as FormGroup;

      console.log(depreciationCostsFormGroup);
      
      console.log("if sts: ", !depreciationCostsFormGroup.value);
      
  
      if (!depreciationCostsFormGroup.value) {
        entryFormGroup.setControl('depreciationCosts', this.fb.group({
          id: [0],
          registrationNum: [''],
          ifPrivateUse: [false]
        }));

        console.log(entryFormGroup);
        

      }
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

  onValueFormEntryChange(fieldName: string, index: number) {
    
    switch (fieldName) {
      case 'quantity':
      case 'price':
      case 'vatRate':
        const entryArray = this.formEntries.get('formArray') as FormArray
        const quantity = entryArray.controls[index].get('quantity')?.value;
        const price = entryArray.controls[index].get('price')?.value;
        const vatRate = entryArray.controls[index].get('vatRate')?.value;
        entryArray.controls[index].get('vatValue')?.setValue(
          (getVatRateValue(vatRate) / 100) * quantity * price
        );
        break;
    }
  }

  

  recomputeVatValue(entry: InvoiceEntry) {

    entry.vatValue = (getVatRateValue(getVatRateFromString(entry.vatRate)) / 100) * entry.quantity * entry.price;
  }

  addEntry() {
    // Create a new entry with default values
  const newEntry = new InvoiceEntry(0, "", 0, 0, 0, Vat.VAT_0 , null);

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

  deleteEntryFromEditedInvoice(index: number): void {
    const formArray = this.formEntries.get('formArray') as FormArray;
  
    if (formArray && formArray.controls.length > index) {
      formArray.removeAt(index);
    }
  }

  addNewEntry() {
    const newEntry = this.fb.group({
      // Define form controls for a new entry
      description: [''],
      quantity: [0],
      price: [0],
      vatRate: [''],
      vatValue: [0],
      depreciationCosts: [null],
    });
  
    (this.formEntries.get('formArray') as FormArray).push(newEntry);
  }

  editInvoice(invoice: Invoice): void {
    this.editingInvoice = invoice;
    // Set default values in the form when editing
    this.setFormValues();
  }

  addCarForEntryForm(index: number) {
    this.addCarForEditedEntry(index);
 //   this.setFormValues();
  }

  trackByFn(index: number, item: any) {   
    return index; 
  }

  // Method to set default values in the form when editing
  private setFormValues(): void {

    if (this.editingInvoice) {
      const entryControls = this.editingInvoice.entries.map(entry => this.fb.group({
        id: [entry.id],
        description: [entry.description],
        quantity: [entry.quantity],
        price: [entry.price],
        vatRate: [entry.vatRate],
        vatValue: [entry.vatValue],
        depreciationCosts: entry.depreciationCosts !== null ? this.fb.group({
          id: [entry.depreciationCosts?.id],
          registrationNum: [entry.depreciationCosts?.registrationNum],
          ifPrivateUse: [entry.depreciationCosts?.ifPrivateUse],
        }) : [null]
      }));
  
      this.invoiceForm = this.fb.group({
        number: [this.editingInvoice.number],
        buyer: [this.editingInvoice.buyer],
        seller: [this.editingInvoice.seller],
        buyerName: [this.editingInvoice.buyer.name],
        sellerName: [this.editingInvoice.seller.name],
        date: [this.editingInvoice.date],
      });
  
      this.formEntries = this.fb.group({
        formArray: this.fb.array([...entryControls])
      });

    }

  }

  saveEditedInvoice() {
    // Update the invoice details
    const buyerName = this.invoiceForm.value.buyerName
    const sellerName = this.invoiceForm.value.sellerName

    delete this.invoiceForm.value.buyerName
    delete this.invoiceForm.value.sellerName

    const formArray = this.formEntries.get('formArray')

    const editedInvoice: Invoice = {
      ...this.editingInvoice!,
      ...this.invoiceForm.value,
      entries: formArray ? formArray.value : this.editingInvoice?.entries , // Update entries as needed
    };

    const buyerCompany = this.companies.find(company => {
      return company.name === buyerName
    })

    const sellerCompany = this.companies.find(company =>{
      return company.name === sellerName
    })

    editedInvoice.buyer = buyerCompany ? buyerCompany : editedInvoice.buyer
    editedInvoice.seller = sellerCompany ? sellerCompany : editedInvoice.seller

    // Call the service to save changes
    this.invoiceService.updateInvoice(editedInvoice).subscribe(() => {
      // Update the invoices list
      const index = this.invoices.findIndex(i => i.id === editedInvoice.id);
      if (index !== -1) {
        this.invoices[index] = editedInvoice;
      }

      // Reset editingInvoice and form
      this.editingInvoice = null;
      this.invoiceForm.reset();
      this.formEntries.reset();

    });
  }

  cancelEditing() {
    // Reset editingInvoice and form
    this.editingInvoice = null;
    this.invoiceForm.reset();
    this.formEntries.reset();
  }

}
//TODO adding new entries to edited invoice 
//TODO change vat value when vat rate is changed
//TODO fix frontedn bugs
