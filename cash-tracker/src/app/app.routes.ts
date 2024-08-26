import { Routes } from '@angular/router';

import {IsAuthenticatedGuard} from "./core/guards/guards";

import {SignupViewComponent} from "./users/views/signup-view/signup-view.component";
import {LoginViewComponent} from "./users/views/login-view/login-view.component";
import {DashboardViewComponent} from "./dashboard/views/dashboard-view/dashboard-view.component";
import {CategoriesListComponent} from "./categories/views/categories-list/categories-list.component";
import {CategoryCreateViewComponent} from "./categories/views/category-create-view/category-create-view.component";
import {CategoryUpdateViewComponent} from "./categories/views/category-update-view/category-update-view.component";
import {AccountCreateViewComponent} from "./accounts/views/account-create-view/account-create-view.component";
import {AccountUpdateViewComponent} from "./accounts/views/account-update-view/account-update-view.component";
import {AccountsListViewComponent} from "./accounts/views/accounts-list-view/accounts-list-view.component";
import {
  TransactionCreateViewComponent
} from "./transactions/views/transaction-create-view/transaction-create-view.component";
import {
  TransactionUpdateViewComponent
} from "./transactions/views/transaction-update-view/transaction-update-view.component";
import {
  TransactionsListViewComponent
} from "./transactions/views/transactions-list-view/transactions-list-view.component";

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
  {
    path: 'accounts/create',
    component: AccountCreateViewComponent,
    canActivate: [IsAuthenticatedGuard()]
  },
  {
    path: 'accounts/update/:id',
    component: AccountUpdateViewComponent,
    canActivate: [IsAuthenticatedGuard()]
  },
  {
    path: 'accounts',
    component: AccountsListViewComponent,
    canActivate: [IsAuthenticatedGuard()]
  },
  {
    path: 'transactions/create',
    component: TransactionCreateViewComponent,
    canActivate: [IsAuthenticatedGuard()]
  },
  {
    path: 'transactions/update/:id',
    component: TransactionUpdateViewComponent,
    canActivate: [IsAuthenticatedGuard()]
  },
  {
    path: 'transactions',
    component: TransactionsListViewComponent,
    canActivate: [IsAuthenticatedGuard()]
  }
];
