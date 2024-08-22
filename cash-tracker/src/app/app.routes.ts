import { Routes } from '@angular/router';

import {IsAuthenticatedGuard} from "./core/guards/guards";

import {SignupViewComponent} from "./users/views/signup-view/signup-view.component";
import {LoginViewComponent} from "./users/views/login-view/login-view.component";
import {DashboardViewComponent} from "./dashboard/views/dashboard-view/dashboard-view.component";
import {CategoriesListComponent} from "./categories/views/categories-list/categories-list.component";
import {CategoryCreateViewComponent} from "./categories/views/category-create-view/category-create-view.component";
import {CategoryUpdateViewComponent} from "./categories/views/category-update-view/category-update-view.component";

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
  },
  {
    path: 'categories/create',
    component: CategoryCreateViewComponent,
    canActivate: [IsAuthenticatedGuard()]
  },
  {
    path: 'categories/update/:id',
    component: CategoryUpdateViewComponent,
    canActivate: [IsAuthenticatedGuard()]
  },
  {
    path: 'categories',
    component: CategoriesListComponent,
    canActivate: [IsAuthenticatedGuard()]
  },
];
