import { ILane } from 'app/shared/model/lane.model';
import { ActionType } from 'app/shared/model/enumerations/action-type.model';

export interface IAccessProfile {
  id?: number;
  name?: string | null;
  description?: string | null;
  startTimeOfDay?: number | null;
  endTimeOfDay?: number | null;
  dayOfWeek?: number | null;
  action?: keyof typeof ActionType | null;
  lanes?: ILane[] | null;
}

export const defaultValue: Readonly<IAccessProfile> = {};
