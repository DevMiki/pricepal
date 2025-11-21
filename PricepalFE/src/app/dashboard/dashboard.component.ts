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
import { ItemFilterCriteriaRequest, ItemResponseDTO } from '@models';
import { ItemService } from '@services/item.service';
import { ItemDataSource } from './item.datasource';
import { MatSort, MatSortModule, SortDirection } from '@angular/material/sort';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { MatPaginator, MatPaginatorModule, PageEvent } from '@angular/material/paginator';

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
    MatPaginatorModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
@UntilDestroy()
export class DashboardComponent implements OnInit, AfterViewInit {
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  displayedColumns: ItemTableColumn[] = [
    'name',
    'price',
    'supermarket',
    'notes',
  ];

  private itemService = inject(ItemService);
  dataSource = new ItemDataSource(this.itemService);

  protected query = {
    sort: {
      active: 'name' as ItemTableColumn,
      direction: 'asc' as SortDirection,
    },
    page: 0,
    size: 10,
  };

  ngOnInit(): void {
    this.loadItems();
  }

  protected filters: ItemFilterCriteriaRequest = {
    nameContains: null,
    supermarketContains: null,
    notesContains: null,
    priceEquals: null,
    priceMin: null,
    priceMax: null
  }

  ngAfterViewInit(): void {
    this.sort.sortChange.pipe(untilDestroyed(this)).subscribe((sortEvent) => {
      this.query.sort.active = sortEvent.active as ItemTableColumn;
      this.query.sort.direction = (sortEvent.direction ||
        'asc') as SortDirection;

      this.query.page = 0;
      this.paginator.firstPage();

      this.loadItems();
    });

    this.paginator.page
    .pipe(untilDestroyed(this))
    .subscribe((pageEvent: PageEvent) => {
      this.query.page = pageEvent.pageIndex,
      this.query.size = pageEvent.pageSize;
      this.loadItems();
    })
  }

  loadItems(): void {
    this.dataSource.loadItems(
        this.filters,
        `${this.query.sort.active},${this.query.sort.direction}`,
        this.query.page,
        this.query.size
      );
  }
}
