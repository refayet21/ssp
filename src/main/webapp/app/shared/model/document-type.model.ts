import { DocumentMasterType } from 'app/shared/model/enumerations/document-master-type.model';

export interface IDocumentType {
  id?: number;
  name?: string;
  isActive?: boolean | null;
  description?: string | null;
  documentMasterType?: keyof typeof DocumentMasterType;
  requiresVerification?: boolean | null;
}

export const defaultValue: Readonly<IDocumentType> = {
  isActive: false,
  requiresVerification: false,
};
