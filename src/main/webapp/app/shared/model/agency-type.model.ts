export interface IAgencyType {
  id?: number;
  name?: string | null;
  shortName?: string | null;
  isActive?: boolean | null;
}

export const defaultValue: Readonly<IAgencyType> = {
  isActive: false,
};
