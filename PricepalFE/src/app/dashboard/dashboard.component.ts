import { AsyncPipe, JsonPipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ItemResponseDTO } from '@models';
import { ItemService } from '@services/item.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [AsyncPipe, JsonPipe],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {

  items$: Observable<ItemResponseDTO[]> | undefined;

  constructor(private itemService: ItemService){}

  ngOnInit(): void {
    this.items$ = this.itemService.fetchAllItems();
  }
}
