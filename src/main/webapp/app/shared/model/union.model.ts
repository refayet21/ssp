import { IUpazila } from 'app/shared/model/upazila.model';

export interface IUnion {
  id?: number;
  name?: string;
  upazila?: IUpazila;
}

export const defaultValue: Readonly<IUnion> = {};
