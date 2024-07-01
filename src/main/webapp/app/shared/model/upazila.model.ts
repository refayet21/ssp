import { IDistrict } from 'app/shared/model/district.model';

export interface IUpazila {
  id?: number;
  name?: string;
  district?: IDistrict;
}

export const defaultValue: Readonly<IUpazila> = {};
