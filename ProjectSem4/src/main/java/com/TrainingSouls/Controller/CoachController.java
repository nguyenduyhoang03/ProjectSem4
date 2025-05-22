package com.TrainingSouls.Controller;

import com.TrainingSouls.DTO.Request.ChangePassword;
import com.TrainingSouls.DTO.Request.UserCreationReq;
import com.TrainingSouls.DTO.Request.UserProfileDTO;
import com.TrainingSouls.DTO.Request.UserUpdate;
import com.TrainingSouls.DTO.Response.*;
import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Service.CoachStudentService;
import com.TrainingSouls.Service.UserProfileService;
import com.TrainingSouls.Service.UserService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Getter;
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
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/coach")
@RequiredArgsConstructor
public class CoachController {
    private final CoachStudentService coachStudentService;

    @GetMapping("/getAllCoach")
    public List<CoachResponseDTO> getAllCoach() {
        return coachStudentService.getAllCoach();
    }

    @GetMapping("/getWorkoutResults/{studentId}")
    @PreAuthorize("hasRole('COACH')")
    public List<WorkoutResultDTO> getWorkoutResultOfStudent(HttpServletRequest request,@PathVariable long studentId) {
        return coachStudentService.getWorkoutResultOfStudent(request,studentId);
    }


    @GetMapping("/getStudentProfile/{studentId}")
    @PreAuthorize("hasRole('COACH')")
    public UserProfileDTO getStudentProfile(HttpServletRequest request, @PathVariable long studentId) {
        return coachStudentService.getStudentProfile(request,studentId);
    }

    @GetMapping("/students")
    public ResponseEntity<List<CoachStudentsDTO>> getStudents(HttpServletRequest request) {
        List<CoachStudentsDTO> students = coachStudentService.getStudentsForCoach(request);
        return ResponseEntity.ok(students);
    }


    @PostMapping("coachConfirmLevelUp/{studentId}")
    public void coachConfirmLevelUp(HttpServletRequest request, @PathVariable long studentId){
        coachStudentService.coachConfirmLevelUp(request,studentId);
    }

}
