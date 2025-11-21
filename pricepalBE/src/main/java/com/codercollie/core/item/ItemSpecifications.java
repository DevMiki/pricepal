package com.codercollie.core.item;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Objects;

public final class ItemSpecifications {

    private ItemSpecifications() {

    }

    public static Specification<Item> fromCriteria(ItemFilterCriteria itemFilterCriteria){
        if(Objects.isNull(itemFilterCriteria)){
            return alwaysTrue();
        }

        Specification<Item> itemSpecification = alwaysTrue();

        itemSpecification = andIf(itemSpecification, itemFilterCriteria.hasNameFilter(), ilike("name", itemFilterCriteria.nameContains()));

        itemSpecification = andIf(itemSpecification, itemFilterCriteria.hasSupermarketFilter(),
                ilike("supermarket", itemFilterCriteria.supermarketContains()));

        itemSpecification = andIf(itemSpecification, itemFilterCriteria.hasNotesFilter(), ilike("notes", itemFilterCriteria.notesContains()));

        final boolean priceEqualsOnly = itemFilterCriteria.usePriceEqualsOnly();
        final boolean priceRangeOnly = itemFilterCriteria.usePriceRangeOnly();

        itemSpecification = andIf(itemSpecification, priceEqualsOnly, priceEquals(itemFilterCriteria.priceEquals()));

        itemSpecification = andIf(itemSpecification, priceRangeOnly && itemFilterCriteria.hasPriceMinFilter(), priceGreaterThanOrEqualTo(itemFilterCriteria.priceMin()));

        itemSpecification = andIf(itemSpecification, priceRangeOnly && itemFilterCriteria.hasPriceMaxFilter(), priceLessThanOrEqualTo(itemFilterCriteria.priceMax()));

        return itemSpecification;
    }

    private static Specification<Item> andIf(Specification<Item> baseSpec, boolean conditionForNewSpec, Specification<Item> specToCompound) {
        if(conditionForNewSpec){
            return baseSpec.and(specToCompound);
        }
        return baseSpec;
    }

    private static Specification<Item> alwaysTrue(){
        return (root, query, cb) -> cb.conjunction();
    }

    private static Specification<Item> ilike(String field, String value) {
        return (root, query, cb) -> cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%");
    }

    private static Specification<Item> priceEquals(BigDecimal price) {
        return (root, query, cb) -> cb.equal(root.get("price"), price);
    }

    private static Specification<Item> priceGreaterThanOrEqualTo(BigDecimal minPrice) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    private static Specification<Item> priceLessThanOrEqualTo(BigDecimal maxPrice) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), maxPrice);
    }
}
