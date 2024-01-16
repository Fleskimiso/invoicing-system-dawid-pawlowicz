// car.model.ts
export class Car {
    public id: number;
    public registrationNum: string;
    public ifPrivateUse: boolean;
  
    constructor(id: number, registrationNum: string, ifPrivateUse: boolean) {
      this.id = id;
      this.registrationNum = registrationNum;
      this.ifPrivateUse = ifPrivateUse;
    }
  }
  