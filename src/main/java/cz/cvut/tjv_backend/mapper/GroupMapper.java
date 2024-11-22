package cz.cvut.tjv_backend.mapper;

import cz.cvut.tjv_backend.dto.group.CreateGroupDto;
import cz.cvut.tjv_backend.dto.group.GroupDto;
import cz.cvut.tjv_backend.dto.group.GroupUpdateDto;
import cz.cvut.tjv_backend.entity.Group;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface GroupMapper {
    Group toEntity(GroupDto groupDto);

    Group toEntityFromCreateGroupDto(CreateGroupDto createGroupDto);

    GroupDto toDto(Group group);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Group partialUpdate(GroupDto groupDto, @MappingTarget Group group);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Group partialUpdate(CreateGroupDto groupDto, @MappingTarget Group group);

    Group toEntity(GroupUpdateDto groupUpdateDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Group partialUpdate(GroupUpdateDto groupUpdateDto, @MappingTarget Group group);
}