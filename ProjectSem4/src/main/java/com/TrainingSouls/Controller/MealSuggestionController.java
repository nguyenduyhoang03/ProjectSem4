package com.TrainingSouls.Controller;

import com.TrainingSouls.DTO.Response.ApiResponse;
import com.TrainingSouls.Service.MealSuggestionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/meals")
@RequiredArgsConstructor
public class MealSuggestionController {

    private final MealSuggestionService mealSuggestionService;

    @GetMapping("/suggest")
    public ApiResponse<String> suggestMeal(HttpServletRequest request, @RequestParam LocalDate date) {
        String meal = mealSuggestionService.suggestMeal(request, date);
        return ApiResponse.<String>builder()
                .result(meal)
                .message("Gợi ý thực đơn thành công")
                .build();
    }
}

