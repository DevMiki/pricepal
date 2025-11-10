import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ItemResponseDTO, PageResponse } from '../models';
import { Observable } from 'rxjs';
import { environment } from 'environments/environment.dev';

@Injectable({
  providedIn: 'root'
})
export class ItemService {

  BASE_API_PATH = `${environment.basePrefix}/items`;

  constructor(private httpClient: HttpClient) { }

  fetchAllItems(filter = '', sort = 'name,asc', page = 0, size = 3): Observable<PageResponse<ItemResponseDTO>> {
    const params = new HttpParams()
    .set('filter', filter)
    .set('sort', sort)
    .set('page', page.toString())
    .set('size', size.toString());
    return this.httpClient.get<PageResponse<ItemResponseDTO>>(this.BASE_API_PATH, { params })
  }
}
