import { Routes } from '@angular/router';

import { MainLayout } from './layouts/main-layout/main-layout';
import { ErrorLayout } from './layouts/error-layout/error-layout';

import { Login } from './pages/login/login';
import { Profile } from './pages/profile/profile';
import { Register } from './pages/register/register';
import { Rentals } from './pages/rentals/rentals';
import { Watches } from './pages/watches/watches';
import { WatchDetails } from './pages/watch-details/watch-details';
import { Home } from './pages/home/home';

import { authGuard } from './core/guards/auth-guard';

import { Admin } from './pages/admin/admin';
import { AdminUsers } from './pages/admin/admin-users/admin-users';
import { AdminBranches } from './pages/admin/admin-branches/admin-branches';
import { AdminRentals } from './pages/admin/admin-rentals/admin-rentals';
import { AdminWatches } from './pages/admin/admin-watches/admin-watches';

import { Branches } from './pages/branches/branches';
import { RentalCreate } from './pages/rental-create/rental-create';
import { WatchCreate } from './pages/watch-create/watch-create';
import { BranchCreate } from './pages/branch-create/branch-create';

import { ErrorSomethingWentWrong } from './pages/errors/error-something-went-wrong/error-something-went-wrong';

export const routes: Routes = [

  // =========================
  // NORMALNY LAYOUT
  // =========================

  {
    path: '',
    component: MainLayout,
    children: [

      {
        path: '',
        component: Home
      },

      {
        path: 'login',
        component: Login
      },

      {
        path: 'register',
        component: Register
      },

      {
        path: 'branches',
        component: Branches
      },

      {
        path: 'branches/create',
        component: BranchCreate
      },

      {
        path: 'profile',
        component: Profile,
        canActivate: [authGuard]
      },

      {
        path: 'rentals',
        component: Rentals,
        canActivate: [authGuard]
      },

      {
        path: 'rentals/:id',
        component: Rentals,
        canActivate: [authGuard]
      },

      {
        path: 'rentals/create/:id',
        component: RentalCreate,
        canActivate: [authGuard]
      },

      {
        path: 'admin',
        component: Admin,
        canActivate: [authGuard]
      },

      {
        path: 'admin/users',
        component: AdminUsers,
        canActivate: [authGuard]
      },

      {
        path: 'admin/watches',
        component: AdminWatches,
        canActivate: [authGuard]
      },

      {
        path: 'admin/branches',
        component: AdminBranches,
        canActivate: [authGuard]
      },

      {
        path: 'admin/rentals',
        component: AdminRentals,
        canActivate: [authGuard]
      },

      {
        path: 'watches',
        component: Watches
      },
      {
        path: 'watches/create',
        component: WatchCreate
      },
    ]
  },


  // =========================
  // ERROR LAYOUT
  // =========================

  {
    path: '',
    component: ErrorLayout,
    children: [

      {
        path: 'error-something-went-wrong',
        component: ErrorSomethingWentWrong
      },

      {
        path: 'forgot-password',
        loadComponent: () =>
          import('./pages/forgot-password/forgot-password')
            .then(m => m.ForgotPassword)
      },
      {
        path: 'reset-password',
        loadComponent: () =>
          import('./pages/reset-password/reset-password')
            .then(m => m.ResetPassword)
      }
    ]
  }

];