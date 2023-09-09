import {browser, by, element, ElementArrayFinder, ElementFinder, WebElement} from 'protractor';

export class CompanyRow {

  constructor(private companyRow: ElementFinder) {
  }

  deleteBtn(): WebElement {
    return this.companyRow.element(by.css('.btn-danger'))
  }

  async taxIdValue(): Promise<string> {
      return this.companyRow.element(by.id('taxId')).getText()
    }

    async nameValue(): Promise<string> {
      return this.companyRow.element(by.id('name')).getText()
    }

    async addressValue(): Promise<string> {
      return this.companyRow.element(by.id('address')).getText()
    }

    async pensionInsuranceValue(): Promise<string> {
      return this.companyRow.element(by.id('pensionInsurance')).getText()
    }

    async healthInsuranceValue(): Promise<string> {
      return this.companyRow.element(by.id('healthInsurance')).getText()
    }

  async assertRowValues(taxId: string, name: string, address: string, pensionInsurance: string, healthInsurance: string) {
    expect(await this.taxIdValue()).toEqual(taxId)
    expect(await this.nameValue()).toEqual(name)
    expect(await this.addressValue()).toEqual(address)
    expect(await this.pensionInsuranceValue()).toEqual(pensionInsurance)
    expect(await this.healthInsuranceValue()).toEqual(healthInsurance)
  }
  async assertNameValue(name: string) {
      expect(await this.nameValue()).toEqual(name)
    }

  async updateCompany(taxId: string, name: string, address: string, pensionInsurance: number, healthInsurance: number) {
    await this.editBtn().click()

    await this.updateTaxIdInput().clear()
    await this.updateTaxIdInput().sendKeys(taxId)

    await this.updateNameInput().clear()
    await this.updateNameInput().sendKeys(name)

    await this.updateAddressInput().clear()
    await this.updateAddressInput().sendKeys(address)

    await this.updatePensionInsuranceInput().clear()
    await this.updatePensionInsuranceInput().sendKeys(pensionInsurance)

    await this.updateHealthInsuranceInput().clear()
    await this.updateHealthInsuranceInput().sendKeys(healthInsurance)

    await this.confirmUpdateCompanyBtn().click()
  }

  private confirmUpdateCompanyBtn() {
    return this.companyRow.element(by.css('.btn-primary'));
  }

  private updateHealthInsuranceInput() {
    return this.companyRow.element(by.css('input[name=healthInsurance]'));
  }

  private updatePensionInsuranceInput() {
    return this.companyRow.element(by.css('input[name=pensionInsurance]'));
  }

  private updateAddressInput() {
    return this.companyRow.element(by.css('input[name=address]'));
  }

  private updateNameInput() {
    return this.companyRow.element(by.css('input[name=name]'));
  }

  private updateTaxIdInput() {
    return this.companyRow.element(by.css('input[name=taxIdentificationNumber]'));
  }

  private editBtn() {
    return this.companyRow.element(by.css('.btn-secondary'));
  }
}
