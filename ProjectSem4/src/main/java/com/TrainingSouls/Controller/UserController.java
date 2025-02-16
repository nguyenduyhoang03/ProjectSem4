package com.TrainingSouls.Controller;

import com.TrainingSouls.DTO.Request.UserCreationReq;
import com.TrainingSouls.DTO.Request.UserUpdate;
import com.TrainingSouls.DTO.Response.ApiResponse;
import com.TrainingSouls.DTO.Response.UserResponse;
import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
     UserService userService;

    @PostMapping("/create-user")
    public ApiResponse<User> createUser(@RequestBody @Valid UserCreationReq user) {
        ApiResponse<User> response = new ApiResponse<>();
        response.setResult(userService.createUser(user));
        return response;
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Name: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info("GrantedAuthority: {}", grantedAuthority));

        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserResponse getUserById(@PathVariable int userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/getMyInfo")
    public ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder().result(userService.getMyInfo()).build();
    }

    @PutMapping("/{userId}")
    public UserResponse UpdateUser(@PathVariable int userId, @RequestBody UserUpdate userUpdate){
        return userService.updateUser(userId,userUpdate);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable int userId) {
        userService.deleteUser(userId);
        return "User deleted";
    }
}
