import { IDivision } from 'app/shared/model/division.model';

export interface IDistrict {
  id?: number;
  name?: string;
  division?: IDivision;
}

export const defaultValue: Readonly<IDistrict> = {};
