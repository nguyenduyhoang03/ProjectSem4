package com.TrainingSouls.Service;

import com.TrainingSouls.DTO.Request.UserProfileDTO;
import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Entity.UserProfile;
import com.TrainingSouls.Repository.UserProfileRepository;
import com.TrainingSouls.Repository.UserRepository;
import com.TrainingSouls.Utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;

    public UserProfileService(UserProfileRepository userProfileRepository, UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public UserProfile saveUserProfile(HttpServletRequest request, UserProfileDTO userProfileDTO) {
        long userId = JWTUtils.getSubjectFromRequest(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        UserProfile userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfile.setGender(userProfileDTO.getGender());
        userProfile.setAge(userProfileDTO.getAge());
        userProfile.setHeight(userProfileDTO.getHeight());
        userProfile.setWeight(userProfileDTO.getWeight());
        userProfile.setActivityLevel(userProfileDTO.getActivityLevel());
        userProfile.setFitnessGoal(userProfileDTO.getFitnessGoal());
        userProfile.setLevel(userProfileDTO.getLevel());
        userProfile.setMedicalConditions(userProfileDTO.getMedicalConditions());

        userProfile.calculateMetrics(); // Tính BMI, Body Fat %, Muscle Mass %

        return userProfileRepository.save(userProfile);
    }
}

