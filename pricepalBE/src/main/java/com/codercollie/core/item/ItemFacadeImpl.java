package com.codercollie.core.item;

import com.codercollie.common.PageResponse;
import com.codercollie.core.item.dto.ItemCreateDTO;
import com.codercollie.core.item.dto.ItemResponseDTO;
import com.codercollie.core.item.dto.ItemUpdateDTO;
import com.codercollie.error.ItemNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ItemFacadeImpl implements ItemFacade{

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    public ItemFacadeImpl(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemResponseDTO createItem(ItemCreateDTO itemCreateDTO) {
        final Item entityToSave = itemMapper.toEntity(itemCreateDTO);
        final Item savedEntity = itemRepository.save(entityToSave);
        return itemMapper.toResponse(savedEntity);
    }

    @Override
    public ItemResponseDTO updateItem(Long id, ItemUpdateDTO itemUpdateDTO) {
        final Item itemToUpdate = itemRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id));
        itemMapper.updateEntityFromDTO(itemUpdateDTO, itemToUpdate);
        final Item savedItem = itemRepository.save(itemToUpdate);
        return itemMapper.toResponse(savedItem);
    }

    @Override
    public void deleteItem(Long id) {
        if (!itemRepository.existsById(id)){
            throw new ItemNotFoundException(id);
        }
        itemRepository.deleteById(id);
    }

    @Override
    public PageResponse<ItemResponseDTO> fetchAllItems(ItemFilterCriteria itemFilterCriteria, Pageable pageable){
        final Sort sorting = pageable.getSort().isSorted()
                ? pageable.getSort()
                : Sort.by(Sort.Direction.ASC, "name");
        final Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sorting);

        final Specification<Item> itemSpecification = ItemSpecifications.fromCriteria(itemFilterCriteria);
        final Page<Item> items = itemRepository.findAll(itemSpecification, pageRequest);
        final Page<ItemResponseDTO> itemPage = items.map(itemMapper::toResponse);
        return PageResponse.from(itemPage);
    }

}
