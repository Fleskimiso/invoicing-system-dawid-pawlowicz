import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from "@angular/common/http"
import { RouterModule } from '@angular/router'; 
import { AppRoutingModule } from './app-routing.module';


import { CompanyComponent } from './companies/app.companies';
import { InvoiceComponent } from './invoices/invoice.component';
import { TaxationComponent } from './taxation/taxation.component';
import { DashboardComponent } from './dashboard/dashboard.component';

@NgModule({
  declarations: [
    CompanyComponent,
    InvoiceComponent,
    TaxationComponent,
    DashboardComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule,
    AppRoutingModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [DashboardComponent]
})
export class AppModule { }
