import { AsyncPipe, TitleCasePipe } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { ItemResponseDTO, PageResponse } from '@models';
import { ItemService } from '@services/item.service';
import { Observable } from 'rxjs';
import { ItemDataSource } from './item.datasource';

type ItemTableColumn = keyof Omit<ItemResponseDTO, 'id'>

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [MatTableModule, TitleCasePipe],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {

  displayedColumns: ItemTableColumn[] = ['name', 'price', 'supermarket', 'notes'];

  private itemService = inject(ItemService);
  dataSource = new ItemDataSource(this.itemService);

  ngOnInit(): void {
    this.dataSource.loadItems()
  }
}
