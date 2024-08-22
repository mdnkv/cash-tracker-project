import {CanActivateFn, Router} from "@angular/router";
import {UserService} from "../../users/services/user.service";
import {inject} from "@angular/core";

export function IsAuthenticatedGuard(): CanActivateFn {
  return () => {
    const userService: UserService = inject(UserService)
    const isAuthenticated: boolean = userService.isAuthenticated.getValue()
    if (isAuthenticated) {
      return true
    } else {
      const router: Router = inject(Router)
      router.navigateByUrl('/users/login')
      return false
    }
  }
}
