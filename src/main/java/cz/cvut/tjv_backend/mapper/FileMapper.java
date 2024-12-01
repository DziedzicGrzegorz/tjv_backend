package cz.cvut.tjv_backend.mapper;

import cz.cvut.tjv_backend.dto.file.FileFullDto;
import cz.cvut.tjv_backend.dto.file.FileDto;
import cz.cvut.tjv_backend.entity.File;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public interface FileMapper {
    File toEntity(FileFullDto fileFullDto);

    FileDto toDto(File file);

    @AfterMapping
    default void linkSharedWithUsers(@MappingTarget File file) {
        file.getSharedWithUsers().forEach(sharedWithUser -> sharedWithUser.setFile(file));
    }

    @AfterMapping
    default void linkSharedWithGroups(@MappingTarget File file) {
        file.getSharedWithGroups().forEach(sharedWithGroup -> sharedWithGroup.setFile(file));
    }

    FileFullDto toFullDto(File file);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    File partialUpdate(FileFullDto fileFullDto, @MappingTarget File file);

    File toEntity(FileDto fileDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    File partialUpdate(FileDto fileDto, @MappingTarget File file);
}