import dayjs from 'dayjs';
import { IAgency } from 'app/shared/model/agency.model';
import { IVehicle } from 'app/shared/model/vehicle.model';

export interface IVehicleAssignment {
  id?: number;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs | null;
  isPrimary?: boolean | null;
  isRental?: boolean | null;
  agency?: IAgency | null;
  vehicle?: IVehicle | null;
}

export const defaultValue: Readonly<IVehicleAssignment> = {
  isPrimary: false,
  isRental: false,
};
