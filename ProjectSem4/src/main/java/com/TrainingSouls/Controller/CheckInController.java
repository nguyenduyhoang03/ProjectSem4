package com.TrainingSouls.Controller;

import com.TrainingSouls.Service.CheckInService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checkin")
@RequiredArgsConstructor
public class CheckInController {
    private final CheckInService checkInService;

    @PostMapping
    public ResponseEntity<String> checkIn(HttpServletRequest request) {
        String message = checkInService.checkIn(request);
        return ResponseEntity.ok(message);
    }
}

