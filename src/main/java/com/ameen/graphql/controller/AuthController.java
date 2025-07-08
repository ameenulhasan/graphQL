package com.ameen.graphql.controller;

import com.ameen.graphql.dto.LoginDto;
import com.ameen.graphql.util.JwtUtil;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class AuthController {

    private final JwtUtil jwtUtil;

    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @MutationMapping
    public Map<String, String> login(@Argument LoginDto loginDto) {
        return jwtUtil.login(loginDto);
    }

    @MutationMapping
    public Map<String, String> refreshToken(@Argument String request ) {
        return jwtUtil.refreshToken(request);
    }

}
