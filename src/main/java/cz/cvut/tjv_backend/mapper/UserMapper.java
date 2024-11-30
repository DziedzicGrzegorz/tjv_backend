package cz.cvut.tjv_backend.mapper;

import cz.cvut.tjv_backend.request.UserCreateRequest;
import cz.cvut.tjv_backend.dto.user.UserDto;
import cz.cvut.tjv_backend.dto.user.UserShortDto;
import cz.cvut.tjv_backend.entity.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserDto userDto);

    UserDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDto userDto, @MappingTarget User user);

    // UserRole to user by default
    @Mapping(target = "roles", expression = "java(java.util.List.of(UserRole.USER))")
    User toEntity(UserCreateRequest userCreateRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserCreateRequest userCreateRequest, @MappingTarget User user);

    User toEntity(UserShortDto userShortDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserShortDto userShortDto, @MappingTarget User user);
}