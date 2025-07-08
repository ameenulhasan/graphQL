package com.ameen.graphql.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListDto {

    @NotNull
    private Long id;
    @NotBlank
    private String name;

    private List<OrderListDto> orders;

}
