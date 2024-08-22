import { Routes } from '@angular/router';

import {IsAuthenticatedGuard} from "./core/guards/guards";

import {SignupViewComponent} from "./users/views/signup-view/signup-view.component";
import {LoginViewComponent} from "./users/views/login-view/login-view.component";
import {DashboardViewComponent} from "./dashboard/views/dashboard-view/dashboard-view.component";

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
    path: 'dashboard',
    component: DashboardViewComponent,
    canActivate: [IsAuthenticatedGuard()]
  }
];
