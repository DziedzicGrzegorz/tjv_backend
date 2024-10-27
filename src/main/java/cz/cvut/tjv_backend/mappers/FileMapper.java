package cz.cvut.tjv_backend.mappers;

import cz.cvut.tjv_backend.dto.FileCreateDto;
import cz.cvut.tjv_backend.dto.FileDto;
import cz.cvut.tjv_backend.entity.File;
import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface FileMapper {
    @Mapping(source = "ownerId", target = "owner.id")
    @Mapping(target = "blobUrl", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "version", constant = "1")
    File toEntity(FileCreateDto fileCreateDto);
    File toEntity(FileDto fileUpdate);
    FileDto toDto(File file);

    File toEntity(MultipartFile fileToUpload);
}