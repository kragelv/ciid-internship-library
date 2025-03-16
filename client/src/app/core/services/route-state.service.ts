import { Injectable } from '@angular/core';
import { NavigationEnd, PRIMARY_OUTLET, Router } from '@angular/router';
import { BehaviorSubject, filter, Observable } from 'rxjs';
import { RouterStateFromType } from '../models/router-state-from.model';

@Injectable({
  providedIn: 'root',
})
export class RouterStateService {
  private routerStateSubject: BehaviorSubject<RouterStateFromType | null>;
  public routerState: Observable<RouterStateFromType | null>;

  constructor(private router: Router) {
    this.routerStateSubject = new BehaviorSubject<RouterStateFromType | null>(
      null
    );
    this.routerState = this.routerStateSubject.asObservable();

    this.initRouteTracking();
  }

  private initRouteTracking(): void {
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        const urlTree = this.router.parseUrl(event.urlAfterRedirects);
        const g = urlTree.root.children[PRIMARY_OUTLET];
        this.routerStateSubject.next({
          path: '/' + g.segments.map((segment) => segment.path).join('/'),
          queryParams: urlTree.queryParams,
          fragment: urlTree.fragment || undefined,
        });
      });
  }
}
