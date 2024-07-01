import { IAgency } from 'app/shared/model/agency.model';
import { IssueChannelType } from 'app/shared/model/enumerations/issue-channel-type.model';
import { TaxCodeType } from 'app/shared/model/enumerations/tax-code-type.model';
import { PassMediaType } from 'app/shared/model/enumerations/pass-media-type.model';
import { PassUserType } from 'app/shared/model/enumerations/pass-user-type.model';

export interface IPassType {
  id?: number;
  name?: string | null;
  shortName?: string | null;
  isActive?: boolean | null;
  printedName?: string | null;
  issueFee?: number | null;
  renewFee?: number | null;
  cancelFee?: number | null;
  minimumDuration?: number | null;
  maximumDuration?: number | null;
  issueChannelType?: keyof typeof IssueChannelType | null;
  taxCode?: keyof typeof TaxCodeType | null;
  passMediaType?: keyof typeof PassMediaType | null;
  passUserType?: keyof typeof PassUserType | null;
  agencies?: IAgency[] | null;
}

export const defaultValue: Readonly<IPassType> = {
  isActive: false,
};
