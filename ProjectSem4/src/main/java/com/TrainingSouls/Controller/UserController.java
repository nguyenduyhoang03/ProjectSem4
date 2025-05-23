package com.TrainingSouls.Controller;

import com.TrainingSouls.DTO.Request.ChangePassword;
import com.TrainingSouls.DTO.Request.UserCreationReq;
import com.TrainingSouls.DTO.Request.UserProfileDTO;
import com.TrainingSouls.DTO.Request.UserUpdate;
import com.TrainingSouls.DTO.Response.ApiResponse;
import com.TrainingSouls.DTO.Response.CoachResponseDTO;
import com.TrainingSouls.DTO.Response.PurchasedItemResponse;
import com.TrainingSouls.DTO.Response.UserWithScoreResponse;
import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Service.CoachStudentService;
import com.TrainingSouls.Service.UserProfileService;
import com.TrainingSouls.Service.UserService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
     UserService userService;
     UserProfileService userProfileService;
     CoachStudentService coachStudentService;

     @GetMapping("/getMyCoach")
     public CoachResponseDTO getMyCoach(HttpServletRequest request) {
         return userService.getCoachInfor(request);
     }


     @GetMapping("/checkExistCoach")
     public Boolean checkExistCoach(HttpServletRequest request) {
         return userService.checkExistCoach(request);
     }

    @PostMapping("/create-user")
    public ApiResponse<User> createUser(@RequestBody @Valid UserCreationReq user) {
        ApiResponse<User> response = new ApiResponse<>();
        response.setResult(userService.createUser(user));
        return response;
    }

    @GetMapping
    public List<User> getAllUsers() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("Name: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info("GrantedAuthority: {}", grantedAuthority));

        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/getMyInfo")
    public ApiResponse<UserWithScoreResponse> getMyInfo() {
        return ApiResponse.<UserWithScoreResponse>builder().result(userService.getMyInfo()).build();
    }

    @GetMapping("/getMyPurchasedItem")
    public ApiResponse<List<PurchasedItemResponse>> getMyPurchasedItem() {
        return ApiResponse.<List<PurchasedItemResponse>>builder().result(userService.getMyPurchasedItems()).build();
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestBody @Valid ChangePassword request, HttpServletRequest httpServletRequest) throws ParseException, JOSEException {
        return userService.changePassword(request,httpServletRequest);

    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody UserUpdate userUpdate) {
        User updatedUser = userService.updateUser(userId, userUpdate);
        return ResponseEntity.ok(updatedUser);
    }


    @PostMapping("/add-points/{userId}/{points}")
    public ResponseEntity<?> addPoint(@PathVariable Long userId, @PathVariable int points) {
        log.info("Add points: userId={}, points={}", userId, points);

        try {
            userService.addPoints(userId, points);
            return ResponseEntity.ok("Points updated successfully");
        } catch (Exception e) {
            log.error("Failed to add points: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/save-profile")
    public ResponseEntity<?> saveProfile(HttpServletRequest request,@Valid @RequestBody UserProfileDTO userProfile) {
        return ResponseEntity.ok(userProfileService.saveUserProfile(request,userProfile));
    }


    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return "User deleted";
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/select-coach/{coachId}")
    public ResponseEntity<?> selectCoach(@PathVariable Long coachId, HttpServletRequest request) {
        coachStudentService.selectCoachForUser(request, coachId);
        return ResponseEntity.ok("Đăng ký HLV thành công");
    }

}
