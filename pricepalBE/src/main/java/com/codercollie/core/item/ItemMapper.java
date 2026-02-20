package com.codercollie.core.item;

import web.dto.ItemCreateDTO;
import web.dto.ItemResponseDTO;
import web.dto.ItemUpdateDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ItemMapper {
    @Mapping(target = "id", ignore = true)
    Item toEntity(ItemCreateDTO itemCreateDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(ItemUpdateDTO itemUpdateDTO, @MappingTarget Item entity);

    ItemResponseDTO toResponse(Item entity);

    List<ItemResponseDTO> toResponseList(List<Item> entities);
}
