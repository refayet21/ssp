export interface IDesignation {
  id?: number;
  name?: string;
  shortName?: string | null;
  isActive?: boolean | null;
}

export const defaultValue: Readonly<IDesignation> = {
  isActive: false,
};
