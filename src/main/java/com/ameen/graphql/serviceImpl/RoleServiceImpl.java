package com.ameen.graphql.serviceImpl;

import com.ameen.graphql.constant.Constant;
import com.ameen.graphql.dto.RoleDto;
import com.ameen.graphql.exception.CustomGraphQLException;
import com.ameen.graphql.model.Role;
import com.ameen.graphql.repository.RoleRepository;
import com.ameen.graphql.response.PageResponse;
import com.ameen.graphql.response.SuccessResponse;
import com.ameen.graphql.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @Override
    public SuccessResponse<Object> saveOrUpdate(RoleDto roleDto) {
        SuccessResponse<Object> successResponse = new SuccessResponse<>();
        Optional<Role> existingRole = roleRepository.findByRoleName(roleDto.getRoleName());
        if (existingRole.isPresent() && !existingRole.get().getId().equals(roleDto.getId())) {
            throw new CustomGraphQLException(Constant.ROLE_ALREADY_EXIST);
        }
        Role role;
        Optional<Role> roleOptional = roleRepository.findById(roleDto.getId());
        if (roleOptional.isPresent()) {
            role = roleOptional.get();
            role.setRoleName(roleDto.getRoleName());
            role.setIsActive(true);
            role.setDeletedFlag(false);
            successResponse.setStatusMessage(Constant.ROLE_UPDATED);
        } else {
            throw new CustomGraphQLException(Constant.ROLE_NOT_FOUND);
        }
        return successResponse;
    }

    @Override
    public SuccessResponse<Object> getById(Long id) {
        SuccessResponse<Object> successResponse = new SuccessResponse<>();
        Optional<Role> roleOptional = roleRepository.findByRoleIsActiveTrue(id);
        if (roleOptional.isPresent()) {
            Role role = roleOptional.get();
            RoleDto dto = new RoleDto();
            dto.setId(role.getId());
            dto.setRoleName(role.getRoleName());
            successResponse.setStatusMessage(Constant.ROLE_ID_FOUND);
            successResponse.setStatusCode(200);
            successResponse.setData(dto);
        } else {
            throw new CustomGraphQLException(Constant.ROLE_NOT_FOUND);
        }
        return successResponse;
    }

    @Override
    public PageResponse<List<RoleDto>> pagesSearch(String search, int pageNo, int pageSize) {
        PageResponse<List<RoleDto>> pageResponse = new PageResponse<>();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        Page<Role> rolePage;
        List<RoleDto> roleDtoList = new ArrayList<>();
        if (search != null && !search.isEmpty()) {
            rolePage = roleRepository.findByPageableRoleName(search, pageable);
        } else {
            rolePage = roleRepository.findByRoleIsActive(pageable);
        }
        if (rolePage.hasContent()) {
            for (Role role : rolePage.getContent()) {
                RoleDto roleListDto = new RoleDto();
                roleListDto.setId(role.getId());
                roleListDto.setRoleName(role.getRoleName());
                roleDtoList.add(roleListDto);
            }
            pageResponse.setHasNext(rolePage.hasNext());
            pageResponse.setHasPrevious(rolePage.hasPrevious());
            pageResponse.setTotalRecordCount(rolePage.getTotalElements());
            pageResponse.setTotalPageCount(rolePage.getTotalPages());
            pageResponse.setData(roleDtoList);
        }
        return pageResponse;
    }

    @Override
    public SuccessResponse<Object> deleteRole(Long id) {
        SuccessResponse<Object> successResponse = new SuccessResponse<>();
        Optional<Role> roleOptional = roleRepository.findById(id);
        if (roleOptional.isPresent() && Boolean.TRUE.equals(roleOptional.get().getIsActive())) {
            Role role = roleOptional.get();
            if (Boolean.TRUE.equals(role.getIsActive())) {
                role.setIsActive(false);
                role.setDeletedFlag(true);
                roleRepository.save(role);
                successResponse.setStatusMessage(Constant.ROLE_DELETED);
                successResponse.setStatusCode(200);
            } else {
                throw new CustomGraphQLException(Constant.ROLE_ALREADY_INACTIVE);
            }
        } else {
            throw new CustomGraphQLException(Constant.ROLE_VALID_ID);
        }
        return successResponse;
    }

}
