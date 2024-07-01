import { IDistrict } from 'app/shared/model/district.model';

export interface IPostOffice {
  id?: number;
  name?: string;
  code?: string;
  district?: IDistrict;
}

export const defaultValue: Readonly<IPostOffice> = {};
