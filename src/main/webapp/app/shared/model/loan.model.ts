import { ILoanPrincipal } from 'app/shared/model/loan-principal.model';

export interface ILoan {
  id?: string;
  name?: string;
  loanPrincipals?: ILoanPrincipal[];
}

export const defaultValue: Readonly<ILoan> = {};
