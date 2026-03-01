import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { AddItemComponent } from './items/add-item/add-item.component';
import { ShellLayoutComponent } from './shell-layout/shell-layout.component';
import { AllItemsTableComponent } from './items/all-items-table/all-items-table.component';
import { AllItemsPageComponent } from './all-items-page/all-items-page.component';
import { AuthCallbackComponent } from './auth-callback/auth-callback.component';

export const routes: Routes = [
  { path: 'auth/callback', component: AuthCallbackComponent},
  {
    path: '',
    component: ShellLayoutComponent,
    children: [
      { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
      { path: 'dashboard', component: DashboardComponent },
      { path: 'add-item', component: AddItemComponent },
      { path: 'all-items', component: AllItemsPageComponent },
    ],
  },
];
