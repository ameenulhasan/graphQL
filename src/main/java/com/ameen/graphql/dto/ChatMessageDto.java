package com.ameen.graphql.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatMessageDto {

    @NotNull
    private Long id;
    @NotNull
    private Long sender;
    @NotNull
    private Long receiver;
    @NotBlank
    private String content;
    @NotNull
    private LocalDateTime timestamp;

}
