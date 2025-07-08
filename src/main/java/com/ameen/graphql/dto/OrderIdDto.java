package com.ameen.graphql.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Setter
@Getter
public class OrderIdDto {

    @NotNull
    private Long orderId;
    @NotNull
    private Integer quantity;
    @NotNull
    private Long totalPrice;
    @NotBlank
    private String userName;
    @NotBlank
    private String bookName;
    @NotBlank
    private String author;
    @NotBlank
    private String price;

}
