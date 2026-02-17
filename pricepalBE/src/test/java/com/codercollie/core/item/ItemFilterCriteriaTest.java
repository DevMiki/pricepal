package com.codercollie.core.item;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ItemFilterCriteriaTest {

    @Test
    void usePriceRangeOnly_returnsTrue_whenPriceMaxExistsAndPriceEqualsIsNull(){
        final ItemFilterCriteria itemFilterCriteria = new ItemFilterCriteria(null, null, null, new BigDecimal("1.00"), null, null, false);

        assertTrue(itemFilterCriteria.usePriceRangeOnly());
    }

    @Test
    void usePriceRangeOnly_returnsFalse_whenPriceMinAndPriceMaxDoNotExist(){
        final ItemFilterCriteria itemFilterCriteria = new ItemFilterCriteria(null, null, null, null, null, null, false);

        assertFalse(itemFilterCriteria.usePriceRangeOnly());
    }

    @Test
    void usePriceRangeOnly_returnsTrue_whenPriceMinExistsAndPriceEqualsIsNull(){
        final ItemFilterCriteria itemFilterCriteria = new ItemFilterCriteria(null, null, new BigDecimal("1.00"), null, null, null, false);

        assertTrue(itemFilterCriteria.usePriceRangeOnly());
    }

    @Test
    void usePriceRangeOnly_returnsFalse_whenPriceEqualsExists(){
        final ItemFilterCriteria itemFilterCriteria = new ItemFilterCriteria(null, new BigDecimal("1.00"), null, null, null, null, false);

        assertFalse(itemFilterCriteria.usePriceRangeOnly());
    }

    @Test
    void hasNameFilter_returnsFalse_whenNameIsBlank(){
        final ItemFilterCriteria itemFilterCriteria = new ItemFilterCriteria("  ", null, null, null, null, null, false);

        assertFalse(itemFilterCriteria.hasNameFilter());
    }

    @Test
    void hasNameFilter_returnsFalse_whenNameIsNull(){
        final ItemFilterCriteria itemFilterCriteria = new ItemFilterCriteria(null, null, null, null, null, null, false);

        assertFalse(itemFilterCriteria.hasNameFilter());
    }

    @Test
    void hasNameFilter_returnsTrue_whenNameExists(){
        final ItemFilterCriteria itemFilterCriteria = new ItemFilterCriteria("Funghetti", null, null, null, null, null, false);

        assertTrue(itemFilterCriteria.hasNameFilter());
    }

    @Test
    void hasSupermarketFilter_returnsFalse_whenSupermarketIsBlank(){
        final ItemFilterCriteria itemFilterCriteria = new ItemFilterCriteria(null, null, null, null, "  ", null, false);

        assertFalse(itemFilterCriteria.hasSupermarketFilter());
    }

    @Test
    void hasSupermarketFilter_returnsFalse_whenSupermarketIsNull(){
        final ItemFilterCriteria itemFilterCriteria = new ItemFilterCriteria(null, null, null, null, null, null, false);

        assertFalse(itemFilterCriteria.hasSupermarketFilter());
    }

    @Test
    void hasSupermarketFilter_returnsTrue_whenSupermarketExists(){
        final ItemFilterCriteria itemFilterCriteria = new ItemFilterCriteria(null, null, null, null, "Conad", null, false);

        assertTrue(itemFilterCriteria.hasSupermarketFilter());
    }

    @Test
    void hasNotesFilter_returnsFalse_whenNotesIsBlank(){
        final ItemFilterCriteria itemFilterCriteria = new ItemFilterCriteria(null, null, null, null, null, "  ", false);

        assertFalse(itemFilterCriteria.hasNotesFilter());
    }

    @Test
    void hasNotesFilter_returnsFalse_whenNotesIsNull(){
        final ItemFilterCriteria itemFilterCriteria = new ItemFilterCriteria(null, null, null, null, null, null, false);

        assertFalse(itemFilterCriteria.hasNotesFilter());
    }

    @Test
    void hasNotesFilter_returnsTrue_whenNotesExists(){
        final ItemFilterCriteria itemFilterCriteria = new ItemFilterCriteria(null, null, null, null, null, "Amazing funghetti bro!", false);

        assertTrue(itemFilterCriteria.hasNotesFilter());
    }

    @Test
    void hasPriceMinFilter_returnsTrue_whenPriceMinExists(){
        final ItemFilterCriteria itemFilterCriteria = new ItemFilterCriteria(null, null, new BigDecimal("1.00"), null, null, null, false);

        assertTrue(itemFilterCriteria.hasPriceMinFilter());
    }

    @Test
    void hasPriceMinFilter_returnsFalse_whenPriceMinIsNull(){
        final ItemFilterCriteria itemFilterCriteria = new ItemFilterCriteria(null, null, null, null, null, null, false);

        assertFalse(itemFilterCriteria.hasPriceMinFilter());
    }

    @Test
    void hasPriceMaxFilter_returnsTrue_whenPriceMaxExists(){
        final ItemFilterCriteria itemFilterCriteria = new ItemFilterCriteria(null, null, null, new BigDecimal("1.00"), null, null, false);

        assertTrue(itemFilterCriteria.hasPriceMaxFilter());
    }

    @Test
    void hasPriceMaxFilter_returnsFalse_whenPriceMaxIsNull(){
        final ItemFilterCriteria itemFilterCriteria = new ItemFilterCriteria(null, null, null, null, null, null, false);

        assertFalse(itemFilterCriteria.hasPriceMaxFilter());
    }

    @Test
    void usePriceEqualsOnly_returnsTrue_whenPriceEqualsExists(){
        final ItemFilterCriteria itemFilterCriteria = new ItemFilterCriteria(null, new BigDecimal("1.00"), null, null, null, null, false);

        assertTrue(itemFilterCriteria.usePriceEqualsOnly());
    }

    @Test
    void usePriceEqualsOnly_returnsFalse_whenPriceEqualsIsNull(){
        final ItemFilterCriteria itemFilterCriteria = new ItemFilterCriteria(null, null, null, null, null, null, false);

        assertFalse(itemFilterCriteria.usePriceEqualsOnly());
    }
}
