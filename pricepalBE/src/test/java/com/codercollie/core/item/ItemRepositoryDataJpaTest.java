package com.codercollie.core.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class ItemRepositoryDataJpaTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void findAll_withNameFilter_returnsOnlyMatchingItems() {
        itemRepository.saveAllAndFlush(List.of(
                new Item(null, "Banana", new BigDecimal("1.20"), "Conad", "yellow"),
                new Item(null, "Mango", new BigDecimal("2.20"), "Famila", "sweet"),
                new Item(null, "Kiwi", new BigDecimal("3.00"), "Lidl", "green")
        ));

        final ItemFilterCriteria criteria = new ItemFilterCriteria("man", null, null, null, null, null, false);
        final List<Item> items = itemRepository.findAll(ItemSpecifications.fromCriteria(criteria));

        assertThat(items).extracting(Item::getName).containsExactly("Mango");
    }
}
