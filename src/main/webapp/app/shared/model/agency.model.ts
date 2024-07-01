import { IAgencyType } from 'app/shared/model/agency-type.model';
import { IPassType } from 'app/shared/model/pass-type.model';

export interface IAgency {
  id?: number;
  name?: string;
  shortName?: string | null;
  isInternal?: boolean | null;
  isDummy?: boolean | null;
  agencyType?: IAgencyType;
  passTypes?: IPassType[] | null;
}

export const defaultValue: Readonly<IAgency> = {
  isInternal: false,
  isDummy: false,
};
