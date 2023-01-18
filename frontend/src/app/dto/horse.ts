import {Owner} from './owner';
import {Sex} from './sex';

export interface Horse {
  id?: number;
  name: string;
  description?: string;
  dateOfBirth: Date | undefined;
  sex: Sex;
  owner?: Owner;
  mother?: Horse;
  father?: Horse;
}


export interface HorseSearch {
  name?: string;
  description?: string;
  bornBefore?: Date;
  sex?: Sex;
  owner?: Owner;
}

export interface HorseSearch1 {
  name?: string;
  description?: string;
  bornBefore?: Date;
  mother?: Horse;
  father?: Horse;
}
