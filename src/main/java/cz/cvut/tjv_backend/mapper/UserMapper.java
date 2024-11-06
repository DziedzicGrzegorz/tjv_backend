package cz.cvut.tjv_backend.mapper;

import cz.cvut.tjv_backend.dto.user.UserCreateDto;
import cz.cvut.tjv_backend.dto.user.UserDto;
import cz.cvut.tjv_backend.dto.user.UserShortDto;
import cz.cvut.tjv_backend.entity.User;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserDto userDto);

//    @AfterMapping
//    default void linkGroupRoles(@MappingTarget User user) {
//        user.getGroupRoles().forEach(groupRole -> groupRole.setUser(user));
//    }
//
//    @AfterMapping
//    default void linkSharedFiles(@MappingTarget User user) {
//        user.getSharedFiles().forEach(sharedFile -> sharedFile.setSharedWith(user));
//    }

    UserDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDto userDto, @MappingTarget User user);

    User toEntity(UserCreateDto userCreateDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserCreateDto userCreateDto, @MappingTarget User user);

    User toEntity(UserShortDto userShortDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserShortDto userShortDto, @MappingTarget User user);
}