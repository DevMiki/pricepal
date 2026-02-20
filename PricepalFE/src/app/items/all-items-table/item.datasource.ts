import { CollectionViewer, DataSource } from "@angular/cdk/collections";
import { ItemFilterCriteriaRequest, ItemResponseDTO, PageResponse } from "@models";
import { ItemService } from "@services/item.service";
import { BehaviorSubject, catchError, finalize, map, Observable, of } from "rxjs";

type SlimPage<T> = Pick<PageResponse<T>, 'content' | 'totalElements'>

export class ItemDataSource implements DataSource<ItemResponseDTO> {

    private itemSubject = new BehaviorSubject<ItemResponseDTO[]>([]);

    private loadingSubject = new BehaviorSubject<boolean>(false);
    public isLoading$ = this.loadingSubject.asObservable();

    private totalSubject = new BehaviorSubject<number>(0);
    public totalItems$ = this.totalSubject.asObservable();

    constructor(private readonly itemService: ItemService){}

    connect(_: CollectionViewer): Observable<readonly ItemResponseDTO[]> {
        return this.itemSubject.asObservable();
    }
    disconnect(_: CollectionViewer): void {
        this.itemSubject.complete();
        this.loadingSubject.complete();
        this.totalSubject.complete();
    }

    loadItems(sort = 'name,asc', page = 0, size = 10, filters: ItemFilterCriteriaRequest = {}){
        this.loadingSubject.next(true);
        this.itemService.fetchAllItems(sort, page, size, filters)
        .pipe(
            map((pageResponse: PageResponse<ItemResponseDTO>) : SlimPage<ItemResponseDTO> => 
                ({
                    content: pageResponse.content,
                    totalElements: pageResponse.totalElements
                })),
            catchError(() => of({content: [], totalElements: 0} as SlimPage<ItemResponseDTO>)),
            finalize(() => this.loadingSubject.next(false))
        )
        .subscribe(({content, totalElements}) => {
            this.itemSubject.next(content)
            this.totalSubject.next(totalElements)
        })
    }
}