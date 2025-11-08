import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ItemService {

  BASE_API_PATH = "items";

  constructor(private httpClient: HttpClient) { }

  fetchAllItems(){
    return this.httpClient.get(this.BASE_API_PATH)
  }
}
