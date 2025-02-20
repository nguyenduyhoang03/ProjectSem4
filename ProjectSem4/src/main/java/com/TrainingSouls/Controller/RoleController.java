package com.TrainingSouls.Controller;

import com.TrainingSouls.DTO.Request.PermissionRequest;
import com.TrainingSouls.DTO.Request.RoleRequest;
import com.TrainingSouls.DTO.Response.ApiResponse;
import com.TrainingSouls.DTO.Response.PermissionResponse;
import com.TrainingSouls.DTO.Response.RoleResponse;
import com.TrainingSouls.Service.PermissionService;
import com.TrainingSouls.Service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;


    @PostMapping
    ApiResponse<RoleResponse> create(@RequestBody RoleRequest roleRequest) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.create(roleRequest))
                .build();
    }

    @GetMapping
    ApiResponse<List<RoleResponse>> getAll() {
        return ApiResponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{role}")
    ApiResponse<Void> delete(@PathVariable("role") String role) {
        roleService.delete(role);
        return ApiResponse.<Void>builder().build();
    }

}
