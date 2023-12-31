import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { Company } from "./company";
import { environment } from "src/environments/environment";
import { Injectable } from "@angular/core";

const PATH = environment.apiUrl +  "/companies"

@Injectable({
    providedIn: "root"
})
export class CompanyService {
    private options = {
        headers: new HttpHeaders({"Content-Type" : "application/json"}),
        withCredentials: true
    }

    constructor(private httpClient: HttpClient) {

    }

    getCompanies(): Observable<Company[]> {
        return this.httpClient.get<Company[]>(PATH);
    }

    addCompany(company: Company): Observable<number> {
        return this.httpClient.post<any>(PATH,this.formalizeCompanyObject(company), this.options );
    }

    deleteCompany(id: number): Observable<any> {
        return this.httpClient.delete<any>(PATH + `/${id}`, this.options);
    }

    editCompany(company: Company): Observable<any> {
        return this.httpClient.put<Company>(PATH + `/${company.id}`, this.formalizeCompanyObject(company), this.options )
    }

    private formalizeCompanyObject(company: Company) {
        return {
            taxIdentificationNumber: company.taxIdentificationNumber,
            address: company.address,
            name: company.name,
            pensionInsurance: company.pensionInsurance,
            healthInsurance: company.healthInsurance
        }
    }
}

