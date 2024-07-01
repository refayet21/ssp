import { ICityCorpPoura } from 'app/shared/model/city-corp-poura.model';
import { IUnion } from 'app/shared/model/union.model';
import { IPostOffice } from 'app/shared/model/post-office.model';
import { ICountry } from 'app/shared/model/country.model';
import { IPerson } from 'app/shared/model/person.model';
import { IAgency } from 'app/shared/model/agency.model';
import { AddressType } from 'app/shared/model/enumerations/address-type.model';

export interface IAddress {
  id?: number;
  addressLineOne?: string;
  addressLineTwo?: string | null;
  addressLineThree?: string | null;
  addressType?: keyof typeof AddressType;
  cityCorpPoura?: ICityCorpPoura | null;
  union?: IUnion | null;
  postOffice?: IPostOffice;
  country?: ICountry | null;
  person?: IPerson | null;
  agency?: IAgency | null;
}

export const defaultValue: Readonly<IAddress> = {};
