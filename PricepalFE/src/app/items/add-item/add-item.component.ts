import { Component, inject } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Router } from '@angular/router';
import { ItemCreateDTO } from '@models';
import { ItemService } from '@services/item.service';
import { ItemFormComponent } from '../item-form/item-form.component';

type FieldKey = 'name' | 'price' | 'supermarket' | 'notes';

@Component({
  selector: 'app-add-item',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ItemFormComponent,
  ],
  templateUrl: './add-item.component.html',
  styleUrl: './add-item.component.scss',
})
export class AddItemComponent {
  private itemService = inject(ItemService);
  private router = inject(Router);
  isSubmitting = false;

  onSubmit(itemCreateDTO: ItemCreateDTO) {
    if (this.isSubmitting) return;

    this.isSubmitting = true;

    this.itemService.createItem(itemCreateDTO).subscribe({
      next: () => {
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        this.isSubmitting = false;
      },
    });
  }
}
