import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { ICountry } from 'app/shared/model/country.model';
import { LogStatusType } from 'app/shared/model/enumerations/log-status-type.model';

export interface IPerson {
  id?: number;
  name?: string;
  shortName?: string | null;
  dob?: dayjs.Dayjs;
  email?: string | null;
  isBlackListed?: boolean | null;
  fatherName?: string | null;
  motherName?: string | null;
  logStatus?: keyof typeof LogStatusType | null;
  internalUser?: IUser | null;
  nationality?: ICountry | null;
}

export const defaultValue: Readonly<IPerson> = {
  isBlackListed: false,
};
