package cz.cvut.tjv_backend.mapper;

import cz.cvut.tjv_backend.request.CreateGroupRequest;
import cz.cvut.tjv_backend.dto.group.GroupDto;
import cz.cvut.tjv_backend.request.GroupUpdateRequest;
import cz.cvut.tjv_backend.entity.Group;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface GroupMapper {
    Group toEntity(GroupDto groupDto);

    Group toEntityFromCreateGroupDto(CreateGroupRequest createGroupRequest);

    GroupDto toDto(Group group);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Group partialUpdate(GroupDto groupDto, @MappingTarget Group group);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Group partialUpdate(CreateGroupRequest groupDto, @MappingTarget Group group);

    Group toEntity(GroupUpdateRequest groupUpdateRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Group partialUpdate(GroupUpdateRequest groupUpdateRequest, @MappingTarget Group group);
}