package cz.cvut.tjv_backend.mapper;

import cz.cvut.tjv_backend.dto.SharedFileWithGroupDto;
import cz.cvut.tjv_backend.entity.SharedFileWithGroup;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {FileMapper.class})
public interface SharedFileWithGroupMapper {
    SharedFileWithGroup toEntity(SharedFileWithGroupDto sharedFileWithGroupDto);

    SharedFileWithGroupDto toDto(SharedFileWithGroup sharedFileWithGroup);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    SharedFileWithGroup partialUpdate(SharedFileWithGroupDto sharedFileWithGroupDto, @MappingTarget SharedFileWithGroup sharedFileWithGroup);
}