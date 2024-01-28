// invoice.service.ts
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Invoice } from './invoice';
import { environment } from 'src/environments/environment';
import { Injectable } from '@angular/core';
import { Company } from '../companies/company';

const PATH = environment.apiUrl + '/invoices';

@Injectable({
  providedIn: 'root'
})
export class InvoiceService {
  private options = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    withCredentials: true
  };

  constructor(private httpClient: HttpClient) {}

  getInvoices(): Observable<Invoice[]> {
    return this.httpClient.get<Invoice[]>(PATH);
  }

  addInvoice(invoice: Invoice): Observable<number> {
    return this.httpClient.post<any>(PATH, this.formalizeInvoiceObject(invoice), this.options);
  }

  deleteInvoice(id: number): Observable<any> {
    return this.httpClient.delete<any>(PATH + `/${id}`, this.options);
  }

  updateInvoice(invoice: Invoice): Observable<Invoice> {
    const id = invoice.id || 0; // Provide a default value for id
    return this.httpClient.put<Invoice>(`${PATH}/${id}`, this.formalizeInvoiceObject(invoice), this.options);
  }
  private formalizeInvoiceObject(invoice: Invoice) {
    // Dummy company data
    const dummyCompany = new Company(0, 'Dummy Company', '', '', 0, 0);
  
    return {
      number: invoice.number || '', 
      buyer: invoice.buyer || dummyCompany, 
      seller: invoice.seller || dummyCompany,
      date: invoice.date || new Date(), 
      entries: invoice.entries || [] 
    };
  }
  
  
  
}
