package com.mycareportal.identity.mapper;

import java.util.Set;

import org.mapstruct.Mapper;

import com.mycareportal.identity.dto.request.role.RoleRequest;
import com.mycareportal.identity.dto.response.role.RoleResponse;
import com.mycareportal.identity.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
	Role toRole(RoleRequest request);

	RoleResponse toRoleResponse(Role role);

	Set<RoleResponse> toSetRoleResponse(Set<Role> roles);
}
