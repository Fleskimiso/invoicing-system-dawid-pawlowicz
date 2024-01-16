// app-routing.module.ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { CompanyComponent } from './companies/app.companies';
import { InvoiceComponent } from './invoices/invoice.component';
import { TaxationComponent } from './taxation/taxation.component';
import { DashboardComponent } from './dashboard/dashboard.component';

const routes: Routes = [
  { path: 'companies', component: CompanyComponent },
  { path: 'invoices', component: InvoiceComponent },
  { path: 'taxation', component: TaxationComponent },
  { path: '', redirectTo: '/', pathMatch: 'full' },
  { path: '**', redirectTo: '/' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
