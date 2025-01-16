package com.mycareportal.identity.mapper;

import org.mapstruct.Mapper;

import com.mycareportal.identity.dto.request.RoleRequest;
import com.mycareportal.identity.dto.response.RoleResponse;
import com.mycareportal.identity.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
	Role toRole(RoleRequest request);
	RoleResponse toRoleResponse(Role role);
}
