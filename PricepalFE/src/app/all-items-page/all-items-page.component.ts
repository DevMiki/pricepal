import { Component } from '@angular/core';
import { AllItemsTableComponent } from "app/items/all-items-table/all-items-table.component";

@Component({
  selector: 'app-all-items-page',
  standalone: true,
  imports: [AllItemsTableComponent],
  templateUrl: './all-items-page.component.html',
  styleUrl: './all-items-page.component.scss'
})
export class AllItemsPageComponent {

}
