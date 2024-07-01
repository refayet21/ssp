import { IZone } from 'app/shared/model/zone.model';
import { GateType } from 'app/shared/model/enumerations/gate-type.model';

export interface IGate {
  id?: number;
  name?: string | null;
  shortName?: string | null;
  lat?: number | null;
  lon?: number | null;
  gateType?: keyof typeof GateType | null;
  isActive?: boolean | null;
  zone?: IZone | null;
}

export const defaultValue: Readonly<IGate> = {
  isActive: false,
};
