package com.codercollie.core.item;

import com.codercollie.common.PageResponse;
import com.codercollie.core.item.dto.ItemCreateDTO;
import com.codercollie.core.item.dto.ItemResponseDTO;
import com.codercollie.core.item.dto.ItemUpdateDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemFacade itemFacade;

    public ItemController(ItemFacade itemFacade) {
        this.itemFacade = itemFacade;
    }

    @PostMapping
    public ResponseEntity<ItemResponseDTO> create(@Valid @RequestBody ItemCreateDTO itemCreateDTO){
        final ItemResponseDTO itemCreated = itemFacade.createItem(itemCreateDTO);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(itemCreated.id())
                .toUri();

        return ResponseEntity.created(location).body(itemCreated);
    }

    @GetMapping
    public PageResponse<ItemResponseDTO> fetchAll(Pageable pageable){
        return itemFacade.fetchAllItems(pageable);
    }

    @PutMapping("/{id}")
    public ItemResponseDTO update(@PathVariable Long id, @Valid @RequestBody ItemUpdateDTO itemUpdateDTO){
        return itemFacade.updateItem(id, itemUpdateDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        itemFacade.deleteItem(id);
    }

}
