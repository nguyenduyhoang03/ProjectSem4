package com.TrainingSouls.Mapper;

import com.TrainingSouls.DTO.Request.PermissionRequest;
import com.TrainingSouls.DTO.Response.PermissionResponse;
import com.TrainingSouls.Entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
