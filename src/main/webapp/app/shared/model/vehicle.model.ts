import { IVehicleType } from 'app/shared/model/vehicle-type.model';
import { LogStatusType } from 'app/shared/model/enumerations/log-status-type.model';

export interface IVehicle {
  id?: number;
  name?: string | null;
  regNo?: string | null;
  zone?: string | null;
  category?: string | null;
  serialNo?: string | null;
  vehicleNo?: string | null;
  chasisNo?: string | null;
  isPersonal?: boolean | null;
  isBlackListed?: boolean | null;
  logStatus?: keyof typeof LogStatusType | null;
  vehicleType?: IVehicleType | null;
}

export const defaultValue: Readonly<IVehicle> = {
  isPersonal: false,
  isBlackListed: false,
};
