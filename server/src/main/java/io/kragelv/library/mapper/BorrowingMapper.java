package io.kragelv.library.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import io.kragelv.library.dto.borrowing.BorrowingBookRequestDto;
import io.kragelv.library.dto.borrowing.BorrowingDto;
import io.kragelv.library.dto.borrowing.BorrowingReturnBookResponseDto;
import io.kragelv.library.dto.borrowing.BorrowingUpdateDto;
import io.kragelv.library.model.Borrowing;

@Mapper(componentModel = ComponentModel.SPRING)
public interface BorrowingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "book", ignore = true)
    @Mapping(target = "reader", ignore = true)
    @Mapping(target = "borrowedAt", ignore = true)
    @Mapping(target = "returnedAt", ignore = true)
    Borrowing borrowingBookDtoToBorrowing(BorrowingBookRequestDto dto);

    BorrowingDto borrowingToBorrowingDto(Borrowing borrowing);

    BorrowingReturnBookResponseDto borrowingToBorrowingReturnBookResponseDto(Borrowing borrowing);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "borrowedAt", ignore = true)
    @Mapping(target = "returnedAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    void updateBorrowingFromBorrowingRequestDto(BorrowingUpdateDto dto, @MappingTarget Borrowing borrowing);
}
