package io.kragelv.library.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;

import io.kragelv.library.dto.reader.ReaderDto;
import io.kragelv.library.dto.reader.ReaderRequestDto;
import io.kragelv.library.model.Reader;

@Mapper(componentModel = ComponentModel.SPRING)
public interface ReaderMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "borrowings", ignore = true)
    Reader readerRequestDtoToReader(ReaderRequestDto dto);

    ReaderDto readerToReaderDto(Reader reader);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "borrowings", ignore = true)
    Reader updateReaderFromReaderRequestDto(ReaderRequestDto dto, @MappingTarget Reader reader);

}
