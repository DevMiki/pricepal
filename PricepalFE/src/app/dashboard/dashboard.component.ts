import { AsyncPipe, TitleCasePipe } from '@angular/common';
import {
  AfterViewInit,
  Component,
  inject,
  OnInit,
  ViewChild,
} from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { ItemResponseDTO } from '@models';
import { ItemService } from '@services/item.service';
import { ItemDataSource } from './item.datasource';
import { MatSort, MatSortModule, SortDirection } from '@angular/material/sort';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';

type ItemTableColumn = keyof Omit<ItemResponseDTO, 'id'>;

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    MatTableModule,
    TitleCasePipe,
    MatProgressBarModule,
    AsyncPipe,
    MatSortModule,
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
@UntilDestroy()
export class DashboardComponent implements OnInit, AfterViewInit {
  @ViewChild(MatSort) sort!: MatSort;

  displayedColumns: ItemTableColumn[] = [
    'name',
    'price',
    'supermarket',
    'notes',
  ];

  private itemService = inject(ItemService);
  dataSource = new ItemDataSource(this.itemService);

  private query = {
    filter: '',
    sort: {
      active: 'name' as ItemTableColumn,
      direction: 'asc' as SortDirection,
    },
    page: 0,
    size: 3,
  };

  ngOnInit(): void {
    this.dataSource.loadItems(
      this.query.filter,
      `${this.query.sort.active},${this.query.sort.direction}`,
      this.query.page,
      this.query.size
    );
  }

  ngAfterViewInit(): void {
    this.sort.sortChange.pipe(untilDestroyed(this)).subscribe((sortEvent) => {
      this.query.sort.active = sortEvent.active as ItemTableColumn;
      this.query.sort.direction = (sortEvent.direction ||
        'asc') as SortDirection;

      this.query.page = 0;

      this.dataSource.loadItems(
        this.query.filter,
        `${this.query.sort.active},${this.query.sort.direction}`,
        this.query.page,
        this.query.size
      );
    });
  }
}
