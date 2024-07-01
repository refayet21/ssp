import dayjs from 'dayjs';
import { IPerson } from 'app/shared/model/person.model';
import { IDesignation } from 'app/shared/model/designation.model';
import { IAgency } from 'app/shared/model/agency.model';

export interface IAssignment {
  id?: number;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs | null;
  isPrimary?: boolean | null;
  person?: IPerson;
  designation?: IDesignation;
  agency?: IAgency;
}

export const defaultValue: Readonly<IAssignment> = {
  isPrimary: false,
};
