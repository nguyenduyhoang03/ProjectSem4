package com.TrainingSouls.Controller;

import com.TrainingSouls.DTO.Request.CreateLevelUpgradeTest;
import com.TrainingSouls.DTO.Request.LevelUpgradeTestDTO;
import com.TrainingSouls.Service.LevelUpgradeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/level-upgrade-tests")
@RequiredArgsConstructor
public class LevelUpgradeTestController {

    private final LevelUpgradeService testService;

    @GetMapping
    public List<CreateLevelUpgradeTest> getAllTests() {
        return testService.getAllTests();
    }

    @GetMapping("/{id}")
    public CreateLevelUpgradeTest getTestById(@PathVariable Integer id) {
        return testService.getTestById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CreateLevelUpgradeTest>> createTest(@RequestBody List<CreateLevelUpgradeTest> dtos) {
        List<CreateLevelUpgradeTest> result = testService.createTests(dtos);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CreateLevelUpgradeTest updateTest(@PathVariable Integer id, @RequestBody CreateLevelUpgradeTest dto) {
        return testService.updateTest(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTest(@PathVariable Integer id) {
        testService.deleteTest(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upgrade-level")
    public ResponseEntity<?> upgradeWorkoutPlan(HttpServletRequest httpServletRequest, @RequestBody List<LevelUpgradeTestDTO> userResults) {
        testService.checkAndUpgradeLevel(httpServletRequest,userResults);
        return ResponseEntity.ok("Len cap thanh cong");
    }
}

