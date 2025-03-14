import { Injectable } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { combineLatest } from 'rxjs';
import { RouterStateFromType } from '../models/router-state-from.model';

@Injectable({
  providedIn: 'root',
})
export class RouterStateService {
  private routerStateSubject: BehaviorSubject<RouterStateFromType | null>;
  public routerState: Observable<RouterStateFromType | null>;

  constructor(private route: ActivatedRoute, private router: Router) {
    this.routerStateSubject = new BehaviorSubject<RouterStateFromType | null>(
      null
    );
    this.routerState = this.routerStateSubject.asObservable();

    this.initRouteTracking();
  }

  private initRouteTracking(): void {
    combineLatest([
      this.route.url,
      this.route.queryParams,
      this.route.fragment,
    ]).subscribe(([urlSegments, queryParams, fragment]) => {
      console.log('subscribe router', urlSegments);
      console.log('subscribe router', queryParams);
      console.log('subscribe router', fragment);
      const routerState = {
        path: '/' + urlSegments.join('/'),
        queryParams: queryParams,
        fragment: fragment === null ? undefined : fragment,
      };
      this.routerStateSubject.next(routerState);
    });
  }
}
