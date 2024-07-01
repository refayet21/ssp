import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IDocumentType } from 'app/shared/model/document-type.model';
import { IPerson } from 'app/shared/model/person.model';
import { IVehicle } from 'app/shared/model/vehicle.model';
import { IAgency } from 'app/shared/model/agency.model';

export interface IDocument {
  id?: number;
  isPrimary?: boolean | null;
  serial?: string;
  issueDate?: dayjs.Dayjs | null;
  expiryDate?: dayjs.Dayjs | null;
  filePath?: string | null;
  verifiedBy?: IUser | null;
  documentType?: IDocumentType;
  person?: IPerson | null;
  vehicle?: IVehicle | null;
  agency?: IAgency | null;
}

export const defaultValue: Readonly<IDocument> = {
  isPrimary: false,
};
