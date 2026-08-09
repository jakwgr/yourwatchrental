import { Routes } from '@angular/router';

import {Login} from './pages/login/login';
import {Profile} from './pages/profile/profile';
import {Register} from './pages/register/register';
import {Rentals} from './pages/rentals/rentals';
import {Watches} from './pages/watches/watches';
import {WatchDetails} from './pages/watch-details/watch-details';
import { Home } from './pages/home/home';
import { authGuard } from './core/guards/auth-guard';

export const routes: Routes = [
    {
        path: "login",
        component: Login
    },
    {
        path: "profile",
        component: Profile,
        canActivate: [authGuard]
    },
    {
        path: 'register',
        component: Register
    },
    {
        path: 'rentals',
        component: Rentals,
        canActivate: [authGuard]
    },
    {
        path: 'watches',
        component: Watches
    },
    {
        path: 'watches/:id',
        component: WatchDetails
    },
    {
        path: '',
        component: Home
    }
];
