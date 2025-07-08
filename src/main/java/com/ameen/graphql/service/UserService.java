package com.ameen.graphql.service;

import com.ameen.graphql.dto.UserDto;
import com.ameen.graphql.dto.UserListDto;
import com.ameen.graphql.response.PageResponse;
import com.ameen.graphql.response.SuccessResponse;

import java.util.List;

public interface UserService {

    SuccessResponse<Object> createUser(UserDto userDto);

    SuccessResponse<Object> getAllUsersWithOrders();

    SuccessResponse<Object> getByIdUser(Long id);

    PageResponse<List<UserListDto>> userPagesSearch(String search, int pageNo, int pageSize);

}
