import { Component, Inject, inject, ViewEncapsulation } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef, MatDialogContent, MatDialogTitle } from '@angular/material/dialog';
import { ItemResponseDTO, ItemUpdateDTO } from '@models';
import { ItemService } from '@services/item.service';
import { ItemFormComponent } from "../item-form/item-form.component";

type EditItemData = { item: ItemResponseDTO }

@Component({
  selector: 'app-edit-item-dialog',
  standalone: true,
  imports: [ItemFormComponent, MatDialogContent, MatDialogTitle],
  templateUrl: './edit-item-dialog.component.html',
  styleUrl: './edit-item-dialog.component.scss',
    encapsulation: ViewEncapsulation.None,
})
export class EditItemDialogComponent {

  isSubmitting = false;
  private itemService = inject(ItemService);

  constructor(
    public dialogRef: MatDialogRef<EditItemDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: EditItemData
  ){}

  onSubmit(itemUpdate: ItemUpdateDTO): void {
    if(this.isSubmitting) return;
    this.isSubmitting = true;

    this.itemService.updateItem(this.data.item.id, itemUpdate).subscribe({
      next: (updated) => this.dialogRef.close(updated),
      error: () => (this.isSubmitting = false)
    })
  }
}
