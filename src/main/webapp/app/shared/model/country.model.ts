export interface ICountry {
  id?: number;
  name?: string | null;
  isoCode?: string | null;
}

export const defaultValue: Readonly<ICountry> = {};
