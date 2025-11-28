import { Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { ItemCreateDTO } from '@models';
import { ItemService } from '@services/item.service';
import { ItemFormComponent } from '../item-form/item-form.component';

@Component({
  selector: 'app-add-item',
  standalone: true,
  imports: [
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
