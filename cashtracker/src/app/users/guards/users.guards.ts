import {CanActivateFn, Router} from "@angular/router";
import {inject} from "@angular/core";

export function IsAuthenticatedGuard(): CanActivateFn {
  return () => {
    const isAuthenticated = localStorage.getItem('token') != null
    if (isAuthenticated){
      return true;
    } else {
      const router: Router = inject(Router);
      router.navigateByUrl("/users/login");
      return false;
    }
  }
}
