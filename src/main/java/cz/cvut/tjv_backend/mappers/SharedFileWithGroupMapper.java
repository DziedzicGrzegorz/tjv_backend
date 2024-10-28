package cz.cvut.tjv_backend.mappers;

import cz.cvut.tjv_backend.entity.SharedFileWithGroup;
import cz.cvut.tjv_backend.dto.SharedFileWithGroupDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface SharedFileWithGroupMapper {
    SharedFileWithGroup toEntity(SharedFileWithGroupDto sharedFileWithGroupDto);

    SharedFileWithGroupDto toDto(SharedFileWithGroup sharedFileWithGroup);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    SharedFileWithGroup partialUpdate(SharedFileWithGroupDto sharedFileWithGroupDto, @MappingTarget SharedFileWithGroup sharedFileWithGroup);
}