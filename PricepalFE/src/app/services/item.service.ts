import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {
  ItemCreateDTO,
  ItemFilterCriteriaRequest,
  ItemResponseDTO,
  ItemUpdateDTO,
  PageResponse,
} from '../models';
import { Observable } from 'rxjs';
import { environment } from 'environments/environment.dev';

@Injectable({
  providedIn: 'root',
})
export class ItemService {
  BASE_API_PATH = `${environment.basePrefix}/items`;

  constructor(private httpClient: HttpClient) {}

  fetchAllItems(
    sort: string = 'name,asc',
    page: number = 0,
    size: number = 3,
    filters: ItemFilterCriteriaRequest = {}
  ): Observable<PageResponse<ItemResponseDTO>> {
    let params = new HttpParams()
      .set('sort', sort)
      .set('page', page.toString())
      .set('size', size.toString());

    const textFilters: Record<string, string | null | undefined> = {
      nameContains: filters.nameContains,
      supermarketContains: filters.supermarketContains,
      notesContains: filters.notesContains,
    };

    Object.entries(textFilters).forEach(([key, value]) => {
      const trimmedValue = value?.trim();
      if (trimmedValue) {
        params = params.set(key, trimmedValue);
      }
    });

    const numericFilters: Record<string, number | null | undefined> = {
      priceEquals: filters.priceEquals,
      priceMin: filters.priceMin,
      priceMax: filters.priceMax,
    };

    Object.entries(numericFilters).forEach(([key, value]) => {
      if (value != null) {
        params = params.set(key, value.toString());
      }
    });

    const isCheapestOnly = filters.cheapestOnly ?? false;
    params = params.set('cheapestOnly', isCheapestOnly);

    return this.httpClient.get<PageResponse<ItemResponseDTO>>(
      this.BASE_API_PATH,
      { params }
    );
  }

  createItem(itemCreateDTO: ItemCreateDTO): Observable<ItemResponseDTO> {
    return this.httpClient.post<ItemResponseDTO>(
      this.BASE_API_PATH,
      itemCreateDTO
    );
  }

  updateItem(id: number, itemUpdateDTO: ItemUpdateDTO): Observable<ItemResponseDTO> {
    return this.httpClient.put<ItemResponseDTO>(
      `${this.BASE_API_PATH}/${id}`,
      itemUpdateDTO
    )
  }

  deleteItem(id: number): Observable<void> {
    return this.httpClient.delete<void>(`${this.BASE_API_PATH}/${id}`)
  }
}
