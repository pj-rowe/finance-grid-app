import { IItem } from 'app/shared/model/item.model';

export interface IBudget {
  id?: string;
  name?: string;
  items?: IItem[];
}

export const defaultValue: Readonly<IBudget> = {};
