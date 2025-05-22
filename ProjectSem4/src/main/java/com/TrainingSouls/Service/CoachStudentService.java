package com.TrainingSouls.Service;

import com.TrainingSouls.DTO.Request.UserProfileDTO;
import com.TrainingSouls.DTO.Response.CoachResponseDTO;
import com.TrainingSouls.DTO.Response.CoachStudentsDTO;
import com.TrainingSouls.DTO.Response.WorkoutResultDTO;
import com.TrainingSouls.Entity.CoachStudent;
import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Entity.UserProfile;
import com.TrainingSouls.Entity.WorkoutResult;
import com.TrainingSouls.Exception.AppException;
import com.TrainingSouls.Exception.ErrorCode;
import com.TrainingSouls.Mapper.UserProfileMapper;
import com.TrainingSouls.Mapper.WorkoutResultMapper;
import com.TrainingSouls.Repository.CoachStudentRepository;
import com.TrainingSouls.Repository.UserProfileRepository;
import com.TrainingSouls.Repository.UserRepository;
import com.TrainingSouls.Repository.WorkoutResultRepository;
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
public class CoachStudentService {
    private final CoachStudentRepository coachStudentRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final WorkoutResultRepository workoutResultRepository;
    private final WorkoutResultMapper workoutResultMapper;
    private final UserProfileMapper userProfileMapper;
    private final WorkoutPlanService workoutPlanService;


    public List<CoachResponseDTO> getAllCoach() {
        List<User> allUsers = userRepository.findAll();

        return allUsers.stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName().equalsIgnoreCase("COACH")))
                .map(user -> {
                    CoachResponseDTO dto = new CoachResponseDTO();
                    dto.setUserID(user.getUserID());
                    dto.setName(user.getName());
                    dto.setEmail(user.getEmail());
                    return dto;
                })
                .toList();
    }



    //xem danh sach hoc vien
    public List<CoachStudentsDTO> getStudentsForCoach(HttpServletRequest req) {
        long coachId = JWTUtils.getSubjectFromRequest(req);

        List<CoachStudent> relations = coachStudentRepository.findByCoach_UserID(coachId);

        return relations.stream()
                .map(CoachStudent::getStudent)
                .map(user -> new CoachStudentsDTO(user.getUserID(), user.getName(), user.getEmail(),user.getUserProfile().getLevel()))
                .collect(Collectors.toList());
    }

    //hoc vien chon COACH
    public void selectCoachForUser(HttpServletRequest request, Long coachId) {
        long studentId = JWTUtils.getSubjectFromRequest(request);
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Người dùng không tồn tại"));

        User coach = userRepository.findById(coachId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "HLV không tồn tại"));

        boolean isCoach = coach.getRoles().stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("COACH"));

        if (!isCoach) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Người này không phải là HLV");
        }

        // Xoá HLV cũ nếu có
        coachStudentRepository.deleteByCoach_UserID(studentId);

        // Tạo liên kết mới
        CoachStudent newRelation = CoachStudent.builder()
                .coach(coach)
                .student(student)
                .build();

        coachStudentRepository.save(newRelation);
    }

    //lay ket qua luyen tap cua hoc vien
    public List<WorkoutResultDTO> getWorkoutResultOfStudent(HttpServletRequest request, long studentId) {
        long coachId = JWTUtils.getSubjectFromRequest(request);

        if (!coachStudentRepository.existsByCoach_UserIDAndStudent_UserID(coachId, studentId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Không có quyền truy cập.");
        }

        List<WorkoutResult> results = workoutResultRepository.findByUserUserID(studentId);

        return results.stream()
                .map(workoutResultMapper::toDTO)
                .collect(Collectors.toList());

    }

    public UserProfileDTO getStudentProfile(HttpServletRequest request, long studentId) {
        long coachId = JWTUtils.getSubjectFromRequest(request);

        if (!coachStudentRepository.existsByCoach_UserIDAndStudent_UserID(coachId, studentId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Không có quyền truy cập.");
        }

        UserProfile userProfile = userProfileRepository.findByUserUserID(studentId).orElseThrow(()->new AppException(ErrorCode.NOT_FOUND));

        return userProfileMapper.toDto(userProfile);
    }


    //COACH xac nhan hoc vien du yeu cau len lv
    public void coachConfirmLevelUp(HttpServletRequest request, Long targetUserId) {
        long coachId = JWTUtils.getSubjectFromRequest(request);
        User coach = userRepository.findById(coachId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coach không tồn tại"));

        if (!coachStudentRepository.existsByCoach_UserIDAndStudent_UserID(coachId, targetUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Không có quyền truy cập.");
        }

        UserProfile userProfile = userProfileRepository.findByUserUserID(targetUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy học viên."));

        String currentLevel = userProfile.getLevel();
        if ("cao cấp".equalsIgnoreCase(currentLevel)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Học viên đã đạt cấp độ cao nhất.");
        }

        // Nâng cấp cấp độ
        if ("người mới".equalsIgnoreCase(currentLevel)) {
            userProfile.setLevel("trung cấp");
        } else if ("trung cấp".equalsIgnoreCase(currentLevel)) {
            userProfile.setLevel("cao cấp");
        }

        userProfileRepository.save(userProfile);

        //xoa lich tap cu va tao lich tap moi
        workoutPlanService.generateWorkoutPlan(targetUserId);
    }


}
