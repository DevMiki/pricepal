import { CollectionViewer, DataSource } from "@angular/cdk/collections";
import { ItemResponseDTO, PageResponse } from "@models";
import { ItemService } from "@services/item.service";
import { BehaviorSubject, catchError, finalize, map, Observable, of } from "rxjs";

export class ItemDataSource implements DataSource<ItemResponseDTO> {

    private itemSubject = new BehaviorSubject<ItemResponseDTO[]>([]);

    private loadingSubject = new BehaviorSubject<boolean>(false);
    public loading$ = this.loadingSubject.asObservable();

    constructor(private readonly itemService: ItemService){}

    connect(_: CollectionViewer): Observable<readonly ItemResponseDTO[]> {
        return this.itemSubject.asObservable();
    }
    disconnect(_: CollectionViewer): void {
        this.itemSubject.complete();
        this.loadingSubject.complete();
    }

    loadItems(filter = '', sort = 'name,asc', page = 0, size = 3){
        this.loadingSubject.next(true);
        this.itemService.fetchAllItems(filter, sort, page, size)
        .pipe(
            map((pageResponse: PageResponse<ItemResponseDTO>) => pageResponse.content),
            catchError(() => of([])),
            finalize(() => this.loadingSubject.next(false))
        )
        .subscribe((content: ItemResponseDTO[]) => {
            this.itemSubject.next(content)}
        )
    }
    
}