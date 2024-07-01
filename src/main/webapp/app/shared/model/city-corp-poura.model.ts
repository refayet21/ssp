import { IDistrict } from 'app/shared/model/district.model';
import { IUpazila } from 'app/shared/model/upazila.model';
import { IRMO } from 'app/shared/model/rmo.model';

export interface ICityCorpPoura {
  id?: number;
  name?: string;
  district?: IDistrict | null;
  upazila?: IUpazila | null;
  rmo?: IRMO;
}

export const defaultValue: Readonly<ICityCorpPoura> = {};
