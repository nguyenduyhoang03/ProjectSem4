package com.TrainingSouls.Service;

import com.TrainingSouls.Entity.CheckIn;
import com.TrainingSouls.Entity.PointsTransaction;
import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Repository.CheckInRepository;
import com.TrainingSouls.Repository.PointsTransactionRepository;
import com.TrainingSouls.Repository.UserRepository;
import com.TrainingSouls.Utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.Jar;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CheckInService {
    private final CheckInRepository checkInRepository;
    private final UserService userService;
    private final PointsTransactionRepository pointsTransactionRepository;

    @Transactional
    public String checkIn(HttpServletRequest request) {
        long userId = JWTUtils.getSubjectFromRequest(request);

        User user = userService.getUserById(userId);

        LocalDate today = LocalDate.now();
        if (checkInRepository.existsByUserAndDate(user, today)) {
            return "Bạn đã điểm danh hôm nay!";
        }

        // Lưu điểm danh
        CheckIn checkIn = new CheckIn();
        checkIn.setUser(user);
        checkIn.setDate(today);
        checkInRepository.save(checkIn);


        PointsTransaction transaction = PointsTransaction.builder()
                .user(user)
                .type(PointsTransaction.TransactionType.EARN)
                .points(100)
                .description("Điểm danh ngày " + today)
                .status(PointsTransaction.TransactionStatus.SUCCESS)
                .date(Instant.now())
                .build();
        pointsTransactionRepository.save(transaction);

        userService.addPoints(userId,100);

        return "Điểm danh thành công!";
    }
}

