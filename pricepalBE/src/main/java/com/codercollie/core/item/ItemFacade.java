package com.codercollie.core.item;

import com.codercollie.common.PageResponse;
import web.dto.ItemCreateDTO;
import web.dto.ItemResponseDTO;
import web.dto.ItemUpdateDTO;
import org.springframework.data.domain.Pageable;

public interface ItemFacade {
    ItemResponseDTO createItem(ItemCreateDTO itemCreateDTO);

    ItemResponseDTO updateItem(Long id, ItemUpdateDTO itemUpdateDTO);

    void deleteItem(Long id);

    PageResponse<ItemResponseDTO> fetchAllItems(ItemFilterCriteria itemFilterCriteria, Pageable pageable);
}