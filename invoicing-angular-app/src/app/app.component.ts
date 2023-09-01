import { Component } from '@angular/core';
import { Company } from './company';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'invoicing-app';

  companies: Company[] = []

  newCompany: Company = new Company("","","",0,0);

  addCompany(){
    this.companies.push(this.newCompany)
    this.newCompany = new Company("","","",0,0);
  }

  deleteCompany(companyToDelete: Company) {
    this.companies = this.companies.filter(company => company !== companyToDelete);
  }

  triggerUpdate(company: Company) {
    company.editedCompany = new Company(
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
    updatedCompany.taxIdentificationNumber = updatedCompany.editedCompany ? 
    updatedCompany.editedCompany.taxIdentificationNumber: updatedCompany.taxIdentificationNumber; 
    updatedCompany.address = updatedCompany.editedCompany ? 
    updatedCompany.editedCompany.address: updatedCompany.address; 
    updatedCompany.name = updatedCompany.editedCompany ? 
    updatedCompany.editedCompany.name: updatedCompany.name; 
    updatedCompany.pensionInsurance = updatedCompany.editedCompany ? 
    updatedCompany.editedCompany.pensionInsurance: updatedCompany.pensionInsurance; 
    updatedCompany.healthInsurance = updatedCompany.editedCompany ? 
    updatedCompany.editedCompany.healthInsurance: updatedCompany.healthInsurance; 

    updatedCompany.editMode = false;
  }
}
