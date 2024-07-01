import { IAgency } from 'app/shared/model/agency.model';

export interface IZone {
  id?: number;
  name?: string | null;
  shortName?: string | null;
  location?: string | null;
  isActive?: boolean | null;
  authority?: IAgency | null;
}

export const defaultValue: Readonly<IZone> = {
  isActive: false,
};
