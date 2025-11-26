import { Routes } from '@angular/router';
import { AllItemsTableComponent } from './items/all-items-table/all-items-table.component';
import { AddItemComponent } from './items/add-item/add-item.component';

export const routes: Routes = [
    { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
    { path: 'dashboard', component: AllItemsTableComponent },
    { path: 'add-item', component: AddItemComponent}
];
