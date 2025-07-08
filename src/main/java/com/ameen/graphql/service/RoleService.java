package com.ameen.graphql.service;


import com.ameen.graphql.dto.RoleDto;
import com.ameen.graphql.response.PageResponse;
import com.ameen.graphql.response.SuccessResponse;

import java.util.List;

public interface RoleService {

    SuccessResponse<Object> saveOrUpdate(RoleDto roleDto);

    SuccessResponse<Object> getById(Long id);

    PageResponse<List<RoleDto>> pagesSearch(String search, int pageNo, int pageSize);

    SuccessResponse<Object> deleteRole(Long id);
}
