package com.TrainingSouls.Service;

import com.TrainingSouls.DTO.Request.CreateLevelUpgradeTest;
import com.TrainingSouls.DTO.Request.LevelUpgradeTestDTO;
import com.TrainingSouls.Entity.LevelUpgradeTest;
import com.TrainingSouls.Entity.UserProfile;
import com.TrainingSouls.Exception.AppException;
import com.TrainingSouls.Exception.ErrorCode;
import com.TrainingSouls.Mapper.LevelUpgradeTestMapper;
import com.TrainingSouls.Repository.LevelUpgradeTestRepository;
import com.TrainingSouls.Repository.UserProfileRepository;
import com.TrainingSouls.Utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LevelUpgradeService {
    private final LevelUpgradeTestRepository levelUpgradeTestRepository;
    private final UserProfileRepository userProfileRepository;
    private final LevelUpgradeTestMapper mapper;
    private final WorkoutPlanService workoutPlanService;

    public List<CreateLevelUpgradeTest> getAllTests() {
        return levelUpgradeTestRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public CreateLevelUpgradeTest getTestById(Integer id) {
        LevelUpgradeTest test = levelUpgradeTestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return mapper.toDTO(test);
    }

    public List<CreateLevelUpgradeTest> createTests(List<CreateLevelUpgradeTest> dtos) {
        List<LevelUpgradeTest> entities = dtos.stream()
                .map(mapper::toEntity)
                .collect(Collectors.toList());

        List<LevelUpgradeTest> savedEntities = levelUpgradeTestRepository.saveAll(entities);

        return savedEntities.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }


    public CreateLevelUpgradeTest updateTest(Integer id, CreateLevelUpgradeTest dto) {
        LevelUpgradeTest existing = levelUpgradeTestRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        existing.setLevel(dto.getLevel());
        existing.setExerciseName(dto.getExerciseName());
        existing.setRequiredReps(dto.getRequiredReps());
        existing.setRequiredSets(dto.getRequiredSets());
        existing.setRequiredDuration(dto.getRequiredDuration());
        existing.setRequiredDistance(dto.getRequiredDistance());

        return mapper.toDTO(levelUpgradeTestRepository.save(existing));
    }

    public void deleteTest(Integer id) {
        if (!levelUpgradeTestRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        levelUpgradeTestRepository.deleteById(id);
    }


    public void checkAndUpgradeLevel(HttpServletRequest request, List<LevelUpgradeTestDTO> userResults) {
        long userId = JWTUtils.getSubjectFromRequest(request);
        UserProfile userProfile = userProfileRepository.findByUserUserID(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "UserProfile not found"));

        String currentLevel = userProfile.getLevel();
        if ("cao cấp".equalsIgnoreCase(currentLevel)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bạn đã đạt cấp độ cao nhất.");
        }

        // Lấy bài test chuẩn cho cấp độ hiện tại
        List<LevelUpgradeTest> standardTests = levelUpgradeTestRepository.findByLevel(currentLevel);

        if (standardTests.size() < 4) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Chưa thiết lập đầy đủ bài test cho cấp độ.");
        }

        // So sánh từng bài test
        for (LevelUpgradeTest test : standardTests) {
            LevelUpgradeTestDTO userTest = userResults.stream()
                    .filter(r -> r.getExerciseName().equalsIgnoreCase(test.getExerciseName()))
                    .findFirst()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Thiếu bài test: " + test.getExerciseName()));

            boolean isPassed;

            if ("Chạy bộ".equalsIgnoreCase(test.getExerciseName())) {
                // So sánh quãng đường chạy và thời gian
                isPassed = userTest.getDistance() >= test.getRequiredDistance() &&
                        userTest.getDuration() >= test.getRequiredDuration();
            } else {
                // So sánh số lần (reps), số set và thời gian
                isPassed = userTest.getReps() >= test.getRequiredReps() &&
                        userTest.getSets() >= test.getRequiredSets() &&
                        userTest.getDuration() >= test.getRequiredDuration();
            }

            if (!isPassed) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không đạt yêu cầu với bài: " + test.getExerciseName());
            }
        }

        // Nếu qua hết thì nâng cấp cấp độ
        if ("người mới".equalsIgnoreCase(currentLevel)) {
            userProfile.setLevel("trung cấp");
        } else if ("trung cấp".equalsIgnoreCase(currentLevel)) {
            userProfile.setLevel("cao cấp");
        }

        userProfileRepository.save(userProfile);

        //tao lich tap cua lv moi
        workoutPlanService.generateWorkoutPlan(request);
    }




}
