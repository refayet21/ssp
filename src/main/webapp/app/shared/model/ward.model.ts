import { ICityCorpPoura } from 'app/shared/model/city-corp-poura.model';
import { IUnion } from 'app/shared/model/union.model';

export interface IWard {
  id?: number;
  name?: string;
  cityCorpPoura?: ICityCorpPoura | null;
  union?: IUnion | null;
}

export const defaultValue: Readonly<IWard> = {};
