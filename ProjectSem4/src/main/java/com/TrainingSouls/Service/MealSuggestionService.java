package com.TrainingSouls.Service;

import com.TrainingSouls.Entity.MealSuggestion;
import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Entity.UserProfile;
import com.TrainingSouls.Exception.AppException;
import com.TrainingSouls.Exception.ErrorCode;
import com.TrainingSouls.Repository.MealSuggestionRepository;
import com.TrainingSouls.Repository.UserRepository;
import com.TrainingSouls.Utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MealSuggestionService {

    private final MealSuggestionRepository mealSuggestionRepository;
    private final ChatService chatService;
    private final UserRepository userRepository;

    public String suggestMeal(HttpServletRequest request, LocalDate date) {
        long userId = JWTUtils.getSubjectFromRequest(request);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));

        // kiểm tra nếu đã có
        Optional<MealSuggestion> existing = mealSuggestionRepository.findByUserAndDate(user, date);
        if (existing.isPresent()) {
            return existing.get().getMeal();
        }

        UserProfile profile = user.getUserProfile();

        String mealPlan = chatService.suggestMeal(profile, date);

        // lưu vào DB
        MealSuggestion suggestion = MealSuggestion.builder()
                .user(user)
                .date(date)
                .meal(mealPlan)
                .fitnessGoal(profile.getFitnessGoal())
                .build();

        mealSuggestionRepository.save(suggestion);
        return mealPlan;
    }

}

