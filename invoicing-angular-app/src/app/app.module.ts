import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from "@angular/common/http"
import { RouterModule } from '@angular/router'; 
import { AppRoutingModule } from './app-routing.module';


import { CompanyComponent } from './companies/app.companies';
import { InvoicesComponent } from './invoices/invoices.component';
import { TaxationComponent } from './taxation/taxation.component';
import { DashboardComponent } from './dashboard/dashboard.component';

@NgModule({
  declarations: [
    CompanyComponent,
    InvoicesComponent,
    TaxationComponent,
    DashboardComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    RouterModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [DashboardComponent]
})
export class AppModule { }
