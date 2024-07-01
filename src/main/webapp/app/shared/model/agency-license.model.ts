import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IAgencyLicenseType } from 'app/shared/model/agency-license-type.model';
import { IAgency } from 'app/shared/model/agency.model';

export interface IAgencyLicense {
  id?: number;
  filePath?: string | null;
  serialNo?: string | null;
  issueDate?: dayjs.Dayjs | null;
  expiryDate?: dayjs.Dayjs | null;
  verifiedBy?: IUser | null;
  agencyLicenseType?: IAgencyLicenseType;
  belongsTo?: IAgency | null;
  issuedBy?: IAgency | null;
}

export const defaultValue: Readonly<IAgencyLicense> = {};
