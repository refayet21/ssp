export interface IVehicleType {
  id?: number;
  name?: string | null;
  numberOfOperators?: number | null;
}

export const defaultValue: Readonly<IVehicleType> = {};
