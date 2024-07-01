import dayjs from 'dayjs';
import { IPass } from 'app/shared/model/pass.model';
import { ILane } from 'app/shared/model/lane.model';
import { DirectionType } from 'app/shared/model/enumerations/direction-type.model';
import { PassStatusType } from 'app/shared/model/enumerations/pass-status-type.model';
import { ActionType } from 'app/shared/model/enumerations/action-type.model';

export interface IEntryLog {
  id?: number;
  eventTime?: dayjs.Dayjs | null;
  direction?: keyof typeof DirectionType | null;
  passStatus?: keyof typeof PassStatusType | null;
  actionType?: keyof typeof ActionType | null;
  pass?: IPass | null;
  lane?: ILane;
}

export const defaultValue: Readonly<IEntryLog> = {};
