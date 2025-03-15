package com.TrainingSouls.Service;

import com.TrainingSouls.DTO.Request.RoleRequest;
import com.TrainingSouls.DTO.Response.RoleResponse;
import com.TrainingSouls.Mapper.RoleMapper;
import com.TrainingSouls.Repository.PermissionRepository;
import com.TrainingSouls.Repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    @PreAuthorize("hasRole('ADMIN')")
    public RoleResponse create(RoleRequest request) {
        var role = roleMapper.toRole(request);
        var permission = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permission));

        role = roleRepository.save(role);

        return roleMapper.toRoleResponse(role);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<RoleResponse> getAll(){
        var roles = roleRepository.findAll();
        return roles.stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(String role) {
        roleRepository.deleteById(role);
    }
}
