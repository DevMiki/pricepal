import { Component, EventEmitter, inject, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ItemFilterCriteriaRequest } from '@models';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { debounceTime } from 'rxjs';

type FiltersFormValue = {
  nameContains: string;
  supermarketContains: string;
  notesContains: string;
  priceEquals: string | number;
  priceMin: string | number;
  priceMax: string | number;
};

@UntilDestroy()
@Component({
  selector: 'app-item-filter',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './item-filter.component.html',
  styleUrl: './item-filter.component.scss',
})
export class ItemFilterComponent {
  @Output() filtersChange = new EventEmitter<ItemFilterCriteriaRequest>();

  private readonly formBuilder = inject(FormBuilder);

  readonly form: FormGroup =
    this.formBuilder.nonNullable.group<FiltersFormValue>({
      nameContains: '',
      supermarketContains: '',
      notesContains: '',
      priceEquals: '',
      priceMin: '',
      priceMax: '',
    });

  constructor() {
    this.form.valueChanges
      .pipe(debounceTime(300), untilDestroyed(this))
      .subscribe((filtersFormValue: FiltersFormValue) => {
        this.filtersChange.emit(
          this.mapToFilterCriteriaRequest(filtersFormValue)
        );
      });
  }

  onReset(): void {
    this.form.reset({
      nameContains: '',
      supermarketContains: '',
      notesContains: '',
      priceEquals: '',
      priceMin: '',
      priceMax: '',
    });
  }

  private toOptionalTrimmedText(value: string): string | null {
    const trimmedValue = value.trim();
    return trimmedValue === '' ? null : trimmedValue;
  }

  private toOptionalNumber(value: string | number): number | null {
    const trimmedValue = String(value).trim();
    if (trimmedValue === '') {
      return null;
    }
    const parsedValue = Number(trimmedValue);
    return Number.isNaN(parsedValue) ? null : parsedValue;
  }

  private mapToFilterCriteriaRequest(
    filtersFormValue: FiltersFormValue
  ): ItemFilterCriteriaRequest {
    return {
      nameContains: this.toOptionalTrimmedText(filtersFormValue.nameContains),
      supermarketContains: this.toOptionalTrimmedText(
        filtersFormValue.supermarketContains
      ),
      notesContains: this.toOptionalTrimmedText(filtersFormValue.notesContains),
      priceEquals: this.toOptionalNumber(filtersFormValue.priceEquals),
      priceMin: this.toOptionalNumber(filtersFormValue.priceMin),
      priceMax: this.toOptionalNumber(filtersFormValue.priceMax),
    };
  }
}
