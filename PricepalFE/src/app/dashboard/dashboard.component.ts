import { AsyncPipe, TitleCasePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { ItemResponseDTO, PageResponse } from '@models';
import { ItemService } from '@services/item.service';
import { Observable } from 'rxjs';

type ItemTableColumn = keyof Omit<ItemResponseDTO, 'id'>

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [AsyncPipe, MatTableModule, TitleCasePipe],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {

  items$: Observable<PageResponse<ItemResponseDTO[]>> | undefined;
  
  displayedColumns: ItemTableColumn[] = ['name', 'price', 'supermarket', 'notes'];

  constructor(private itemService: ItemService){}

  ngOnInit(): void {
    this.items$ = this.itemService.fetchAllItems();
  }
}
