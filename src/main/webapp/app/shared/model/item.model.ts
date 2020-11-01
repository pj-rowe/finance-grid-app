import { Moment } from 'moment';
import { IBudget } from 'app/shared/model/budget.model';
import { ItemType } from 'app/shared/model/enumerations/item-type.model';
import { ItemCategory } from 'app/shared/model/enumerations/item-category.model';

export interface IItem {
  id?: string;
  name?: string;
  itemType?: ItemType;
  dueDate?: string;
  paid?: boolean;
  expectedAmount?: number;
  actualAmount?: number;
  category?: ItemCategory;
  budget?: IBudget;
}

export const defaultValue: Readonly<IItem> = {
  paid: false,
};
