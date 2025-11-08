import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ItemResponseDTO } from '../models';
import { Observable } from 'rxjs';
import { environment } from 'environments/environment.dev';

@Injectable({
  providedIn: 'root'
})
export class ItemService {

  BASE_API_PATH = `${environment.basePrefix}/items`;

  constructor(private httpClient: HttpClient) { }

  fetchAllItems(): Observable<ItemResponseDTO[]>{
    return this.httpClient.get<ItemResponseDTO[]>(this.BASE_API_PATH)
  }

  
}
