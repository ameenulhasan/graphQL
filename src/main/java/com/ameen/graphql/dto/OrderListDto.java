package com.ameen.graphql.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderListDto {

    @NotNull
    private Long id;
    @NotNull
    private Integer quantity;
    @NotNull
    private Long totalPrice;

    private BookIdDto bookIdDto;
    private BookDto book;

}
