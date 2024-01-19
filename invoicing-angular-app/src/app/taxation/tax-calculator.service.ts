// tax-calculator.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TaxCalculatorResult } from './tax-calculator-result.model';
import { environment } from 'src/environments/environment';
import { Company } from '../companies/company';

const PATH = environment.apiUrl + '/tax';

@Injectable({
  providedIn: 'root'
})
export class TaxCalculatorService {
  private apiUrl = PATH; 

  constructor(private http: HttpClient) {}

  calculateTaxes(company: Company): Observable<TaxCalculatorResult> {
    const requestBody = company;
    return this.http.get<TaxCalculatorResult>(this.apiUrl +  `/${company.id}`);
  }

}
