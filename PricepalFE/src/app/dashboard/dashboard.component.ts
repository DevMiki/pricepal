import { Component } from '@angular/core';
import { AllItemsTableComponent } from "app/items/all-items-table/all-items-table.component";

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [AllItemsTableComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent {

}
