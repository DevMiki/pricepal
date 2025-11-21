package com.codercollie.core.item;

import java.math.BigDecimal;

public record ItemFilterCriteria(
        String nameContains,
        BigDecimal priceEquals,
        BigDecimal priceMin,
        BigDecimal priceMax,
        String supermarketContains,
        String notesContains
) {

    public boolean hasNameFilter(){
        return nameContains != null && !nameContains.isBlank();
    }

    public boolean hasSupermarketFilter(){
        return supermarketContains != null && !supermarketContains.isBlank();
    }

    public boolean hasNotesFilter(){
        return notesContains != null && !notesContains.isBlank();
    }

    public boolean hasPriceMinFilter(){
        return priceMin != null;
    }

    public boolean hasPriceMaxFilter(){
        return priceMax != null;
    }

    public boolean usePriceEqualsOnly(){
        return priceEquals != null;
    }

    public boolean usePriceRangeOnly(){
        return priceEquals == null && (priceMin != null || priceMax != null);
    }
}
