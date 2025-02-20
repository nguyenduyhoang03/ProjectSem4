package com.TrainingSouls.Mapper;

import com.TrainingSouls.DTO.Request.RoleRequest;
import com.TrainingSouls.DTO.Response.RoleResponse;
import com.TrainingSouls.Entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest roleRequest);

    RoleResponse toRoleResponse(Role role);
}
