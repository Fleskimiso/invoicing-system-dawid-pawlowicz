// tax-calculator.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { TaxCalculatorResult } from './tax-calculator-result.model';
import { CompanyService } from '../companies/CompanyService'; 
import { TaxCalculatorService } from './tax-calculator.service'; 
import { Company } from '../companies/company';

@Component({
  selector: 'app-taxation',
  templateUrl: './taxation.component.html',
  styleUrls: ['./taxation.component.css']
})
export class TaxationComponent implements OnInit {
  taxCalculatorForm!: FormGroup;
  companies: Company[] = [];
  taxResult!: TaxCalculatorResult;

  constructor(
    private formBuilder: FormBuilder,
    private companyService: CompanyService,
    private taxCalculatorService: TaxCalculatorService
  ) {}

  ngOnInit(): void {
    this.taxCalculatorForm = this.formBuilder.group({
      companyId: [''] // FormControl for selected company
    });

    // Fetch list of companies
    this.companyService.getCompanies().subscribe(companies => {
      this.companies = companies;
    });
  }

  calculateTaxes() {
    const company =  this.companies.filter(comapny => this.taxCalculatorForm.value.companyId == comapny.id)[0];
    this.taxCalculatorService.calculateTaxes(company).subscribe(result => {
      this.taxResult = result;
    });
  }
}
