import { Routes } from '@angular/router';

import {IsAuthenticatedGuard} from "./users/guards/users.guards";

import {SignupViewComponent} from "./users/views/signup-view/signup-view.component";
import {LoginViewComponent} from "./users/views/login-view/login-view.component";
import {CurrentUserViewComponent} from "./users/views/current-user-view/current-user-view.component";

export const routes: Routes = [
  {
    path: 'users/signup',
    component: SignupViewComponent
  },
  {
    path: 'users/login',
    component: LoginViewComponent
  },
  {
    path: 'users/current',
    component: CurrentUserViewComponent,
    canActivate: [IsAuthenticatedGuard()]
  }
];
