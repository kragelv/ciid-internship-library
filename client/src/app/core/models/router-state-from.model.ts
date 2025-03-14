import { Params } from '@angular/router';

export interface RouterStateFromType {
  path: string;
  queryParams?: Params;
  fragment?: string;
}
