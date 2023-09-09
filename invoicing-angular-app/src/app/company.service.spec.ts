import { TestBed } from '@angular/core/testing';
import { CompanyService } from './CompanyService';
import { Company } from './company';
import { HttpClient } from "@angular/common/http";
import { HttpClientTestingModule, HttpTestingController } from "@angular/common/http/testing";
import { environment } from 'src/environments/environment';

describe('Company Service Test', () => {
    let httpTestingController: HttpTestingController;
    let companyService: CompanyService;

    beforeEach(async () => {
        TestBed.configureTestingModule({
            imports: [
                HttpClientTestingModule
            ]
        })

        httpTestingController = TestBed.inject(HttpTestingController);
        companyService = TestBed.inject(CompanyService)
    });

    it(`calling getCompanies() should invoke GET companies`, () => {
        companyService.getCompanies().subscribe(companies => expect(companies).toEqual(expectedCompanies))

        const request = httpTestingController.expectOne(`${environment.apiUrl}/companies`);
        expect(request.request.method).toBe("GET");

        request.flush(expectedCompanies);

        httpTestingController.verify();
    });

    it(`calling addCompany() should invoke POST`, () => {
        const company = expectedCompanies[0];
        const expectedId = 99;

        companyService.addCompany(company).subscribe(id => expect(id).toEqual(expectedId))

        const request = httpTestingController.expectOne(`${environment.apiUrl}/companies`);
        expect(request.request.method).toBe("POST");
        expect(request.request.body).toEqual(
            {
                taxIdentificationNumber: '111-111-11-11',
                name: 'First Ltd.',
                address: 'ul. First 1',
                pensionInsurance: 111.11,
                healthInsurance: 1111.11
            }
        )

        request.flush(expectedId);

        httpTestingController.verify();
    });

    const expectedCompanies: Company[] = [
        new Company(
            1,
            "111-111-11-11",
            "ul. First 1",
            "First Ltd.",
            1111.11,
            111.11
        ),
        new Company(
            2,
            "222-222-22-22",
            "ul. Second 2",
            "Second Ltd.",
            2222.22,
            222.22
        )
    ];

});
