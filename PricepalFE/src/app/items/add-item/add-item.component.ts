import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { Router, RouterLink } from "@angular/router";
import { ItemService } from '@services/item.service';

type FieldKey = 'name' | 'price' | 'supermarket' | 'notes'

@Component({
  selector: 'app-add-item',
  standalone: true,
  imports: [ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, RouterLink],
  templateUrl: './add-item.component.html',
  styleUrl: './add-item.component.scss'
})
export class AddItemComponent {

  private itemService = inject(ItemService)
  private router = inject(Router)
  private formBuilder = inject(FormBuilder)
  isSubmitting = false;

  fields: Array<{
    key: FieldKey,
    label: string,
    type?: 'text' | 'number' | 'textarea';
    required?: boolean;
    min?: number;
    step?: number;
  }> = [
    {key: 'name', label: 'Name', required: true },
    {key: 'price', label: 'Price', type: 'number', required: true, min: 0, step: 0.01 },
    {key: 'supermarket', label: 'Supermarket', required: true},
    {key: 'notes', label: 'Notes', type: 'textarea'}
  ]

  form: FormGroup = this.formBuilder.group({
    name: ['', [Validators.required]],
    price: ['', [Validators.required, Validators.min(0)]],
    supermarket: ['', [Validators.required]],
    notes: ['']
  })

  onSubmit(){
    if(this.form.invalid){
      this.form.markAllAsTouched()
    }

    if(this.isSubmitting) return;

    this.isSubmitting = true;
    this.form.disable();

    this.itemService.createItem(this.form.value).subscribe({
      next: () => {
        this.router.navigate(['/dashboard'])
      },
      error: (err) => {
        this.isSubmitting = false;
        this.form.enable();
      }
    })
  }
}
