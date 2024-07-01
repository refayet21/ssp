import dayjs from 'dayjs';
import { IPassType } from 'app/shared/model/pass-type.model';
import { IUser } from 'app/shared/model/user.model';
import { IAssignment } from 'app/shared/model/assignment.model';
import { IVehicleAssignment } from 'app/shared/model/vehicle-assignment.model';
import { PassStatusType } from 'app/shared/model/enumerations/pass-status-type.model';

export interface IPass {
  id?: number;
  collectedFee?: number | null;
  fromDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  status?: keyof typeof PassStatusType | null;
  passNumber?: number | null;
  mediaSerial?: string | null;
  passType?: IPassType;
  requestedBy?: IUser;
  assignment?: IAssignment;
  vehicleAssignment?: IVehicleAssignment | null;
}

export const defaultValue: Readonly<IPass> = {};
