import { Moment } from 'moment';
import { ILoan } from 'app/shared/model/loan.model';

export interface ILoanPrincipal {
  id?: string;
  date?: string;
  amount?: number;
  loan?: ILoan;
}

export const defaultValue: Readonly<ILoanPrincipal> = {};
