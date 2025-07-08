package com.ameen.graphql.controller;

import com.ameen.graphql.dto.RoleDto;
import com.ameen.graphql.response.PageResponse;
import com.ameen.graphql.response.SuccessResponse;
import com.ameen.graphql.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @MutationMapping
    public SuccessResponse<Object> saveOrUpdate (@Valid @Argument RoleDto roleDto) {
        return roleService.saveOrUpdate(roleDto);
    }

    @QueryMapping
    public SuccessResponse<Object> getById(@Argument("id")Long id){
        return roleService.getById(id);
    }

    @QueryMapping
    public PageResponse<List<RoleDto>> pagesSearchListRole(@Argument String search,
                                                           @Argument Integer pageNo,
                                                           @Argument Integer pageSize) {
        int page = (pageNo == null || pageNo <= 0) ? 1 : pageNo;
        int size = (pageSize == null || pageSize <= 0) ? 10 : pageSize;
        String query = (search == null) ? "" : search;
        return roleService.pagesSearch(query, page, size);
    }

    @QueryMapping
    public SuccessResponse<Object> deleteProjectStage(@Argument Long id) {
        return roleService.deleteRole(id);
    }

}
