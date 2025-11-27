import { AsyncPipe, TitleCasePipe } from '@angular/common';
import {
  AfterViewInit,
  Component,
  inject,
  Input,
  OnInit,
  ViewChild,
} from '@angular/core';
import { MatTableModule } from '@angular/material/table';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { ItemFilterCriteriaRequest, ItemResponseDTO } from '@models';
import { ItemService } from '@services/item.service';
import { MatSort, MatSortModule, SortDirection } from '@angular/material/sort';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import {
  MatPaginator,
  MatPaginatorModule,
  PageEvent,
} from '@angular/material/paginator';
import {
  FiltersFormValue,
  ItemFilterComponent,
} from 'app/items/item-filter/item-filter.component';
import { MatChipsModule } from '@angular/material/chips';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { Router } from '@angular/router';
import { ItemDataSource } from './item.datasource';

type ItemTableColumn = keyof Omit<ItemResponseDTO, 'id'>;

type FilterChip = {
  key: string;
  label: string;
  value: string;
};

@Component({
  selector: 'all-items-table',
  standalone: true,
  imports: [
    MatTableModule,
    TitleCasePipe,
    MatProgressBarModule,
    AsyncPipe,
    MatSortModule,
    MatPaginatorModule,
    ItemFilterComponent,
    MatChipsModule,
    MatIconModule,
    MatButtonModule,
  ],
  templateUrl: './all-items-table.component.html',
  styleUrl: './all-items-table.component.scss',
})
@UntilDestroy()
export class AllItemsTableComponent implements OnInit, AfterViewInit {
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(ItemFilterComponent) itemFilterComponent!: ItemFilterComponent;
  @Input() cheapestOnly = false;

  displayedColumns: ItemTableColumn[] = [
    'name',
    'price',
    'supermarket',
    'notes',
  ];

  private router = inject(Router);
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
    this.filters = { ...this.filters, cheapestOnly: this.cheapestOnly}
    this.loadItems();
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
        (this.query.page = pageEvent.pageIndex),
          (this.query.size = pageEvent.pageSize);
        this.loadItems();
      });
  }

  protected filters: ItemFilterCriteriaRequest = {
    nameContains: null,
    supermarketContains: null,
    notesContains: null,
    priceEquals: null,
    priceMin: null,
    priceMax: null,
    cheapestOnly: this.cheapestOnly
  };

  protected activeFilterChips: FilterChip[] = [];

  private updateActiveFilterChips(): void {
    const chips: FilterChip[] = [];

    if (this.filters.nameContains) {
      chips.push({
        key: 'nameContains',
        label: 'Name',
        value: this.filters.nameContains,
      });
    }

    if (this.filters.supermarketContains) {
      chips.push({
        key: 'supermarketContains',
        label: 'Supermarket',
        value: this.filters.supermarketContains,
      });
    }

    if (this.filters.notesContains) {
      chips.push({
        key: 'notesContains',
        label: 'Notes',
        value: this.filters.notesContains,
      });
    }

    if (this.filters.priceEquals != null) {
      chips.push({
        key: 'priceEquals',
        label: 'Price',
        value: `= ${this.filters.priceEquals}`,
      });
    } else {
      if (this.filters.priceMin != null) {
        chips.push({
          key: 'priceMin',
          label: 'Price min',
          value: `= ${this.filters.priceMin}`,
        });
      }
      if (this.filters.priceMax != null) {
        chips.push({
          key: 'priceMax',
          label: 'Price max',
          value: `= ${this.filters.priceMax}`,
        });
      }
    }

    this.activeFilterChips = chips;
  }

  onRemoveFilter(filterChip: FilterChip) {
    this.filters = { ...this.filters, [filterChip.key]: null };
    this.itemFilterComponent.clearFilterField(
      filterChip.key as keyof FiltersFormValue
    );
    this.updateActiveFilterChips();
    this.resetPage();
    this.loadItems();
  }

  onFiltersChange(newFilters: ItemFilterCriteriaRequest): void {
    this.filters = {...newFilters, cheapestOnly: this.cheapestOnly};
    this.updateActiveFilterChips();
    this.resetPage();
    this.loadItems();
  }

  private resetPage(): void {
    this.query.page = 0;
    this.paginator?.firstPage();
  }

  loadItems(): void {
    const mergedFilters = { ...this.filters, cheapestOnly: this.cheapestOnly }
    this.dataSource.loadItems(
      `${this.query.sort.active},${this.query.sort.direction}`,
      this.query.page,
      this.query.size,
      mergedFilters
    );
  }

  addItem() {
    this.router.navigate(['/add-item'])
  }
}
