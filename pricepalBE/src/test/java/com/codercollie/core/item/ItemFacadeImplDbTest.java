package com.codercollie.core.item;

import com.codercollie.common.PageResponse;
import web.dto.ItemResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Testcontainers(disabledWithoutDocker = true)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ItemFacadeImpl.class, ItemMapperImpl.class})
public class ItemFacadeImplDbTest {

    @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16-alpine")
            .withDatabaseName("pricepal-test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.PostgreSQLDialect");
    }

    @Autowired
    private ItemFacadeImpl itemFacade;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void fetchAllItems_returnsPagedResultsFromDatabase_sortedByNameWhenUnsortedPageable(){
        itemRepository.saveAll(List.of(
                new Item(null, "Banana", new BigDecimal("1.20"), "Conad", "buonone queste oh!"),
                new Item(null, "Kiwi", new BigDecimal("2.20"), "Famila", "mica male"),
                new Item(null, "Mango", new BigDecimal("3.00"), "Lidl", "Mango me lo ricordavo questo")
        ));
        itemRepository.flush();

        final Pageable pageable = PageRequest.of(0, 10);
        final PageResponse<ItemResponseDTO> result = itemFacade.fetchAllItems(null, pageable);

        assertEquals(3, result.totalElements());
        assertEquals(3, result.content().size());
        assertEquals("Banana", result.content().get(0).name());
        assertEquals("Kiwi", result.content().get(1).name());
        assertEquals("Mango", result.content().get(2).name());
    }
}
