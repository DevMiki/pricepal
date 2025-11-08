export interface ItemCreateDTO {
    name: string;
    price: number;
    supermarket?: string | null;
    notes?: string | null;
}

export interface ItemUpdateDTO {
    name: string;
    price: number;
    supermarket?: string | null;
    notes?: string | null;
}

export interface ItemResponseDTO {
    id: number;
    name: string;
    price: number;
    supermarket?: string | null;
    notes?: string | null;
}

export interface PageResponse<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
    numberOfElements: number;
    first: boolean;
    last: boolean;
    empty: boolean;
}