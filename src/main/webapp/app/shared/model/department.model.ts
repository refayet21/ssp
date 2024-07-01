import { IAgency } from 'app/shared/model/agency.model';

export interface IDepartment {
  id?: number;
  name?: string | null;
  agency?: IAgency | null;
}

export const defaultValue: Readonly<IDepartment> = {};
