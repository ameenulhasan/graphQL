package com.ameen.graphql.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class EditMessageDto {

    @NotNull
    private Long messageId;
    @NotBlank
    private String content;
}
