package com.ameen.graphql.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Setter
@Getter
public class OrderDto {

    @NotNull
    private Long id;
    @NotNull
    private Long userId;
    @NotNull
    private Long bookId;
    @NotNull
    private Integer quantity;
    @NotBlank
    private String date;

}
