import { IGate } from 'app/shared/model/gate.model';
import { IAccessProfile } from 'app/shared/model/access-profile.model';
import { DirectionType } from 'app/shared/model/enumerations/direction-type.model';

export interface ILane {
  id?: number;
  name?: string | null;
  shortName?: string | null;
  direction?: keyof typeof DirectionType | null;
  isActive?: boolean | null;
  gate?: IGate | null;
  accessProfiles?: IAccessProfile[] | null;
}

export const defaultValue: Readonly<ILane> = {
  isActive: false,
};
