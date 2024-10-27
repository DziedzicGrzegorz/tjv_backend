package cz.cvut.tjv_backend.mappers;

import cz.cvut.tjv_backend.dto.UserCreateDto;
import cz.cvut.tjv_backend.entity.User;
import cz.cvut.tjv_backend.dto.UserDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User toEntity(UserDto userDto);
    User toEntity(UserCreateDto userDto);

    UserDto toDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdate(UserDto userDto, @MappingTarget User user);
}