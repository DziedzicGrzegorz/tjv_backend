package cz.cvut.tjv_backend.mapper;

import cz.cvut.tjv_backend.dto.SharedFileWithUserDto;
import cz.cvut.tjv_backend.entity.SharedFileWithUser;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {FileMapper.class})
public interface SharedFileWithUserMapper {
    SharedFileWithUser toEntity(SharedFileWithUserDto sharedFileWithUserDto);

    SharedFileWithUserDto toDto(SharedFileWithUser sharedFileWithUser);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    SharedFileWithUser partialUpdate(SharedFileWithUserDto sharedFileWithUserDto, @MappingTarget SharedFileWithUser sharedFileWithUser);
}