import { Component, EventEmitter, inject, Input, OnChanges, OnInit, Output, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { ItemCreateDTO, ItemResponseDTO } from '@models';

type FieldKey = 'name' | 'price' | 'supermarket' | 'notes';

@Component({
  selector: 'app-item-form',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
  ],
  templateUrl: './item-form.component.html',
  styleUrl: './item-form.component.scss',
  encapsulation: ViewEncapsulation.None,
})
export class ItemFormComponent implements OnInit, OnChanges {
  @Input() initialValue?: Partial<ItemResponseDTO>;
  @Input() submitLabel = 'Save';
  @Input() cancelLabel = 'Cancel';
  @Input() disableSubmit = false;
  @Input() resetOnCancel = false;
  @Output() isSubmitted = new EventEmitter<ItemCreateDTO>();
  @Output() cancelled = new EventEmitter<void>();
  
  private formBuilder = inject(FormBuilder);
  isSubmitting = false;

  fields: Array<{
    key: FieldKey;
    label: string;
    type?: 'text' | 'number' | 'textarea';
    required?: boolean;
    min?: number;
    step?: number;
  }> = [
    { key: 'name', label: 'Name', required: true },
    {
      key: 'price',
      label: 'Price',
      type: 'number',
      required: true,
      min: 0,
      step: 0.01,
    },
    { key: 'supermarket', label: 'Supermarket', required: true },
    { key: 'notes', label: 'Notes', type: 'textarea' },
  ];
  
  form: FormGroup = this.formBuilder.group({
    name: ['', [Validators.required]],
    price: ['', [Validators.required, Validators.min(0)]],
    supermarket: ['', [Validators.required]],
    notes: [''],
  });
  
  ngOnInit(): void {
    if (this.initialValue){
      this.form.patchValue(this.initialValue);
    }
  }
  
  ngOnChanges(changes: SimpleChanges): void {
    if (changes['initialValue'] && this.initialValue){
      this.form.patchValue(this.initialValue)
    }
  }
  
  onSubmit(){
    if(this.form.invalid){
      this.form.markAllAsTouched();
      return;
    }
    this.isSubmitted.emit(this.form.value as ItemCreateDTO)
  }

  onCancel() {
    if(this.resetOnCancel){
      this.form.reset();
    }
    this.cancelled.emit();
  }
}
