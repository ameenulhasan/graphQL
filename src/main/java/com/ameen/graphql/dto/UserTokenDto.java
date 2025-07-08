package com.ameen.graphql.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserTokenDto {

    private Long id;
    private String email;
    private String userName;
    private long iat;
    private long exp;
    private String sub;
    private String role;

}
