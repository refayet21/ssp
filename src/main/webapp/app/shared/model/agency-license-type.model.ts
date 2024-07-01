export interface IAgencyLicenseType {
  id?: number;
  name?: string;
  isActive?: boolean | null;
}

export const defaultValue: Readonly<IAgencyLicenseType> = {
  isActive: false,
};
