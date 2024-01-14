import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TaxationComponent } from './taxation.component';

describe('TaxationComponent', () => {
  let component: TaxationComponent;
  let fixture: ComponentFixture<TaxationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TaxationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TaxationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
