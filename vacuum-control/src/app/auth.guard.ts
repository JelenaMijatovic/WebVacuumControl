import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  CanActivate,
  CanDeactivate,
  Router,
  RouterStateSnapshot,
  UrlTree
} from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate, CanDeactivate<unknown> {

  constructor(private router: Router) {
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

    if (state.url == "/users/add" && localStorage.getItem('token')) {
      let user = localStorage.getItem('user');
      if (user) {
        let userJ = JSON.parse(user);
        for (let perm of userJ.permissions) {
          if (perm.permission == "can_create_users") {
            return true;
          }
        }
      }
      return false;
    }

    if (state.url == "/users/edit/:email" && localStorage.getItem('token')) {
      let user = localStorage.getItem('user');
      if (user) {
        let userJ = JSON.parse(user);
        for (let perm of userJ.permissions) {
          if (perm.permission == "can_update_users") {
            return true;
          }
        }
      }
      return false;
    }

    if (state.url == "/vacuums/add" && localStorage.getItem('token')) {
      let user = localStorage.getItem('user');
      if (user) {
        let userJ = JSON.parse(user);
        for (let perm of userJ.permissions) {
          if (perm.permission == "can_add_vacuum") {
            return true;
          }
        }
      }
      return false;
    }

    if (localStorage.getItem('token')) {
      return true;
    }

    this.router.navigate(['/auth/login']);
    return false;

  }

  canDeactivate(
    component: unknown,
    currentRoute: ActivatedRouteSnapshot,
    currentState: RouterStateSnapshot,
    nextState?: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return true;
  }

}
