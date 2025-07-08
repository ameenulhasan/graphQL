package com.ameen.graphql.controller;

import com.ameen.graphql.dto.UserDto;
import com.ameen.graphql.dto.UserListDto;
import com.ameen.graphql.response.PageResponse;
import com.ameen.graphql.response.SuccessResponse;
import com.ameen.graphql.service.UserService;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @MutationMapping
    public SuccessResponse<Object> createUser(@Valid @Argument UserDto userDto) {
        return userService.createUser(userDto);
    }

    @QueryMapping
    public SuccessResponse<Object> getAllUsersWithOrders() {
        return userService.getAllUsersWithOrders();
    }

    @QueryMapping
    public SuccessResponse<Object> getByIdUser(@Argument("id") Long id) {
        return userService.getByIdUser(id);
    }

    @QueryMapping
    public PageResponse<List<UserListDto>> userPagesSearch(@Argument String search,
                                                           @Argument int pageNo,
                                                           @Argument int pageSize) {
        return userService.userPagesSearch(search, pageNo, pageSize);
    }

}