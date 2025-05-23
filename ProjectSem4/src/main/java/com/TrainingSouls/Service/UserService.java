package com.TrainingSouls.Service;

import com.TrainingSouls.DTO.Request.ChangePassword;
import com.TrainingSouls.DTO.Request.IntrospectRequest;
import com.TrainingSouls.DTO.Request.UserCreationReq;
import com.TrainingSouls.DTO.Request.UserUpdate;
import com.TrainingSouls.DTO.Response.CoachResponseDTO;
import com.TrainingSouls.DTO.Response.PurchasedItemResponse;
import com.TrainingSouls.DTO.Response.UserResponse;
import com.TrainingSouls.DTO.Response.UserWithScoreResponse;
import com.TrainingSouls.Entity.*;
import com.TrainingSouls.Exception.AppException;
import com.TrainingSouls.Exception.ErrorCode;
import com.TrainingSouls.Mapper.UserMapper;
import com.TrainingSouls.Repository.*;
import com.TrainingSouls.Utils.JWTUtils;
import com.nimbusds.jose.JOSEException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    StoreItemService storeItemService;
    AuthenticationService authenticationService;
    LeaderboardRepository leaderboardRepository;
    VerificationTokenRepository verificationTokenRepository;
    EmailService emailService;
    CoachStudentRepository coachStudentRepository;
    DeviceTokenRepository deviceTokenRepository;


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
        user.setStreak(0);
        user.setActive(false);

        user = userRepository.save(user);

        //tao token xac thuc
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(60));

        verificationTokenRepository.save(verificationToken);

        //gui mail
        String confirmURL = "http://54.251.220.228:8080/trainingSouls/auth/confirm?token=" + token;
        String content = """
            <html>
              <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;">
                <div style="max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 10px; padding: 30px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);">
                  <h2 style="color: #333333;">Chào mừng bạn đến với <span style="color: #4CAF50;">TrainingSouls</span>!</h2>
                  <p style="font-size: 16px; color: #555555;">
                    Cảm ơn bạn đã đăng ký. Link sẽ hết hạn sau 60 phút, Vui lòng nhấn nút bên dưới để xác nhận tài khoản của bạn:
                  </p>
                  <div style="text-align: center; margin: 30px 0;">
                    <a href="%s" style="background-color: #4CAF50; color: white; padding: 12px 24px; text-decoration: none; font-size: 16px; border-radius: 6px; display: inline-block;">
                      Xác nhận tài khoản
                    </a>
                  </div>
                  <p style="font-size: 14px; color: #888888;">
                    Nếu bạn không yêu cầu tạo tài khoản, hãy bỏ qua email này.
                  </p>
                </div>
              </body>
            </html>
            """.formatted(confirmURL);


        emailService.sendHtmlEmail(user.getEmail(),"Xác nhận tài khoản TrainingSouls", content);

        return user;

    }

    //lay thong tin cua coach cho hoc vien
    public CoachResponseDTO getCoachInfor(HttpServletRequest req) {
        long userId = JWTUtils.getSubjectFromRequest(req);

        CoachStudent coachStudent = coachStudentRepository.findByStudentUserID(userId).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND));

        CoachResponseDTO coachResponseDTO = new CoachResponseDTO();
        coachResponseDTO.setName(coachStudent.getCoach().getName());
        coachResponseDTO.setEmail(coachStudent.getCoach().getEmail());

        return coachResponseDTO;
    }


    //check xem user co coach chua
    public Boolean checkExistCoach(HttpServletRequest req) {
        long userId = JWTUtils.getSubjectFromRequest(req);

        return coachStudentRepository.findByStudentUserID(userId).isPresent();

    }



    //lay tat ca User
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> getAllUsers() {
        return userRepository.findAll().stream().toList();
    }


    //lay user theo ID
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
    }


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public User updateUser(Long userId, UserUpdate userUpdate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userUpdate.getName() != null) {
            user.setName(userUpdate.getName());
        }

        if (userUpdate.getRoles() != null && !userUpdate.getRoles().isEmpty()) {
            Set<Role> roles = userUpdate.getRoles().stream()
                    .map(roleName -> roleRepository.findById(roleName)
                            .orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
                    .collect(Collectors.toSet());
            user.setRoles(roles);
        }

        return userRepository.save(user);
    }

    //lay thong tin cua ban than
    public UserWithScoreResponse getMyInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        Double totalScore = leaderboardRepository.findByUser(user)
                .map(Leaderboard::getTotalScore)
                .orElse(0.0); // nếu chưa có leaderboard thì trả về 0.0

        return UserWithScoreResponse.builder()
                .userID(user.getUserID())
                .name(user.getName())
                .email(user.getEmail())
                .accountType(user.getAccountType())
                .points(user.getPoints())
                .level(user.getLevel())
                .streak(user.getStreak())
                .roles(user.getRoles())
                .purchasedItems(user.getPurchasedItems())
                .userProfile(user.getUserProfile())
                .totalScore(totalScore)
                .build();
    }



    //lay san pham da mua
    public List<PurchasedItemResponse> getMyPurchasedItems() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByEmail(name)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        List<PurchasedItemResponse> purchasedItemDTOs = new ArrayList<>();
        for (UserItem userItem : user.getPurchasedItems()) {
            StoreItem item = storeItemService.getById(userItem.getItemId());

            PurchasedItemResponse dto = new PurchasedItemResponse();
            dto.setItemId(userItem.getId());
            dto.setPurchasedAt(LocalDate.from(userItem.getPurchasedAt()));
            dto.setExpirationDate(userItem.getExpirationDate());
            dto.setName(item.getName());

            purchasedItemDTOs.add(dto);
        }

        return purchasedItemDTOs;
    }

    public String changePassword(ChangePassword request, HttpServletRequest httpRequest) throws ParseException, JOSEException {
        long id = JWTUtils.getSubjectFromRequest(httpRequest);
        User user = userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        // 2. Kiểm tra mật khẩu cũ
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_INVALID);
        }

        // 3. Cập nhật mật khẩu
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // 4. Logout token cũ
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            IntrospectRequest logoutRequest = new IntrospectRequest();
            logoutRequest.setToken(token);
            authenticationService.logout(logoutRequest);
        }

        return authenticationService.generateToken(user);
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
