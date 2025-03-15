package com.TrainingSouls.Service;

import com.TrainingSouls.DTO.Request.ChangePassword;
import com.TrainingSouls.DTO.Request.UserCreationReq;
import com.TrainingSouls.DTO.Request.UserUpdate;
import com.TrainingSouls.DTO.Response.UserResponse;
import com.TrainingSouls.Entity.Role;
import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Exception.AppException;
import com.TrainingSouls.Exception.ErrorCode;
import com.TrainingSouls.Mapper.UserMapper;
import com.TrainingSouls.Repository.RoleRepository;
import com.TrainingSouls.Repository.UserRepository;
import com.TrainingSouls.Utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;



    //tao tai khoan - tao user moi
    public User createUser(UserCreationReq request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
       User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findById("USER")
                .orElseThrow(() -> new RuntimeException("Role not found"));
        roles.add(userRole);
        user.setRoles(roles);
        user.setPoints(0);
        user.setLevel(1);
        user.setAccountType("basic");

        return userRepository.save(user);
    }


    //lay tat ca User
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }


    //lay user theo ID
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
    }


    //cap nhat thong tin nguoi dung
    public UserResponse updateUser(Long userId,UserUpdate request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User Not Found"));
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        var roles = roleRepository.findAllById(request.getRoles());

        user.setRoles(new HashSet<>(roles));
        return userMapper.toUserResponse(userRepository.save(user));
    }


    //lay thong tin cua ban than
    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByEmail(name).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        return userMapper.toUserResponse(user);
    }


    public void addPoints(Long userId, Integer points) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        user.setPoints(user.getPoints() + points);
        userRepository.save(user);
    }


    //XOA USER
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }
}
