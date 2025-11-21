package com.codercollie.core.item;

import com.codercollie.common.PageResponse;
import com.codercollie.core.item.dto.ItemCreateDTO;
import com.codercollie.core.item.dto.ItemResponseDTO;
import com.codercollie.core.item.dto.ItemUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemFacade {
    ItemResponseDTO createItem(ItemCreateDTO itemCreateDTO);

    ItemResponseDTO updateItem(Long id, ItemUpdateDTO itemUpdateDTO);

    void deleteItem(Long id);

    PageResponse<ItemResponseDTO> fetchAllItems(ItemFilterCriteria itemFilterCriteria, Pageable pageable);
}