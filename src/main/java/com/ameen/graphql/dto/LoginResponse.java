package com.ameen.graphql.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String role;
}
