
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CompanyComponent } from './app.companies';
import { CompanyService } from './CompanyService';
import { Company } from './company';
import { of } from 'rxjs';
import { FormsModule } from '@angular/forms';

describe('AppComponent', () => {
  let fixture: ComponentFixture<CompanyComponent>;
  let component: CompanyComponent;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        { provide: CompanyService, useClass: MockCompanyService }
      ],
      declarations: [
        CompanyComponent
      ],
      imports: [
        FormsModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CompanyComponent);
    component = fixture.componentInstance;

    component.ngOnInit()
    fixture.detectChanges();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(CompanyComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should display a list of companies`, () => {
      expect(fixture.nativeElement.innerText).toContain("111-111-11-11	ul. First 1	First Ltd.	111.11	1111.11")
      expect(fixture.nativeElement.innerText).toContain("222-222-22-22	ul. Second 2	Second Ltd.	222.22	2222.22")

      expect(component.companies.length).toBe(2)
      expect(component.companies).toBe(MockCompanyService.companies)
    });

  it(`newly added company is added to the list`, () => {
       const taxIdInput: HTMLInputElement = fixture.nativeElement.querySelector("input[name=taxIdentificationNumber]")
       taxIdInput.value = "333-333-33-33"
       taxIdInput.dispatchEvent(new Event('input'));

       const nameInput: HTMLInputElement = fixture.nativeElement.querySelector("input[name=name]")
       nameInput.value = "Third Ltd."
       nameInput.dispatchEvent(new Event('input'));

       const addressInput: HTMLInputElement = fixture.nativeElement.querySelector("input[name=address]")
       addressInput.value = "ul. Third 3"
       addressInput.dispatchEvent(new Event('input'));

       const addInvoiceBtn: HTMLElement = fixture.nativeElement.querySelector("#addCompanyButton")
       addInvoiceBtn.click()

       fixture.detectChanges();
       const compiled = fixture.nativeElement;
       expect(compiled.querySelector('.content span').textContent).toContain('invoicing-angular-app app is running!');

       expect(fixture.nativeElement.innerText).toContain("333-333-33-33	ul. Third 3	Third Ltd.	0	0")
  });

  it(`should have as title 'invoicing-angular-app'`, () => {
    const fixture = TestBed.createComponent(CompanyComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('invoicing-angular-app');
  });

  it('should render title', () => {
    const fixture = TestBed.createComponent(CompanyComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    expect(compiled.querySelector('.content span').textContent).toContain('invoicing-angular-app app is running!');
  });

  class MockCompanyService {
    static companies: Company[] = [
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

    getCompanies() {
      return of(MockCompanyService.companies);
    }

    addCompany(company: Company){
    MockCompanyService.companies.push(company)
    return of()
    }
  }
});
