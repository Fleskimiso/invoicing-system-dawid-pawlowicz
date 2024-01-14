import { Component, OnInit } from '@angular/core';
import { Company } from './company';
import { CompanyService } from './CompanyService';

@Component({
  selector: 'app-companies',
  templateUrl: './app.companies.html',
  styleUrls: ['./app.companies.css']
})
export class CompanyComponent implements OnInit {
  title = 'invoicing-angular-app';

  companies: Company[] = []

  newCompany: Company = new Company(0,"","","",0,0);

  constructor(
    private companiesService: CompanyService
  ) {
  }

  ngOnInit(): void {
    this.companiesService.getCompanies()
    .subscribe(companies =>{
      this.companies = companies
    })
  }

  addCompany(){
    this.companiesService.addCompany(this.newCompany)
    .subscribe(id =>{
      this.newCompany.id = id;
      this.companies.push(this.newCompany)
      this.newCompany = new Company(this.companies.length,"","","",0,0);
    })
  }

  deleteCompany(companyToDelete: Company) {
    this.companiesService.deleteCompany(companyToDelete.id)
    .subscribe(() =>{
      this.companies = this.companies.filter(company => company !== companyToDelete);
    })
  }

  triggerUpdate(company: Company) {
    company.editedCompany = new Company(
      company.id,
      company.taxIdentificationNumber,
      company.address,
      company.name,
      company.healthInsurance,
      company.pensionInsurance
    );

    company.editMode = true;
  }
  cancelCompanyUpdate(company: Company) {
    company.editMode = false;
  }

  updateCompany(updatedCompany: Company) {
    this.companiesService.editCompany(updatedCompany.editedCompany ? updatedCompany.editedCompany: updatedCompany)
    .subscribe(() =>{
      if(updatedCompany.editedCompany) {
        updatedCompany.taxIdentificationNumber = updatedCompany.editedCompany.taxIdentificationNumber
        updatedCompany.address = updatedCompany.editedCompany.address
        updatedCompany.name = updatedCompany.editedCompany.name
        updatedCompany.pensionInsurance = updatedCompany.editedCompany.pensionInsurance
        updatedCompany.healthInsurance = updatedCompany.editedCompany.healthInsurance
      }
    })

    updatedCompany.editMode = false;
  }
}
