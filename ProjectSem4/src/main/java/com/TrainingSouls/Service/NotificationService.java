package com.TrainingSouls.Service;

import com.TrainingSouls.DTO.Request.NotificationContent;
import com.TrainingSouls.Entity.CoachStudent;
import com.TrainingSouls.Entity.Notification;
import com.TrainingSouls.Entity.User;
import com.TrainingSouls.Exception.AppException;
import com.TrainingSouls.Exception.ErrorCode;
import com.TrainingSouls.Repository.CoachStudentRepository;
import com.TrainingSouls.Repository.NotificationRepository;
import com.TrainingSouls.Repository.UserRepository;
import com.TrainingSouls.Utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final CoachStudentRepository coachStudentRepository;
    private final FirebaseMessagingService firebaseMessagingService;


    // lay cac thong bao
    public List<Notification> getNotifications(HttpServletRequest req) {
        long userId = JWTUtils.getSubjectFromRequest(req);

        return notificationRepository.findByReceiverId(userId);
    }

    // danh dau la da doc
    public void markAsRead(long id){
        Notification notification = notificationRepository.findById(id).orElseThrow(()-> new AppException(ErrorCode.NOT_FOUND));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

    //gui thong bao len lich kiem tra
    public void notifyCoachLevelTestRequest(HttpServletRequest request, LocalDateTime dateTime) {
        long studentId = JWTUtils.getSubjectFromRequest(request);

        User student = userRepository.findById(studentId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "khong tim thay hoc vien nay"));

        CoachStudent coachStudent = coachStudentRepository.findByStudentUserID(studentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Học viên chưa được gán Coach"));

        User coach = coachStudent.getCoach();

        String title = "Yêu cầu kiểm tra nâng cấp level";

        String content = "Học viên " + student.getName() + " đã lên lịch kiểm tra cấp độ vào lúc " +
                dateTime.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"));


        Notification notification = new Notification();
        notification.setReceiverId(coach.getUserID());
        notification.setTitle(title);
        notification.setContent(content);
        notificationRepository.save(notification);

        NotificationContent notificationContent = new NotificationContent();
        notificationContent.setTitle(title);
        notificationContent.setBody(content);

        firebaseMessagingService.sendNotificationToUser(coachStudent.getCoach().getUserID(), notificationContent);

    }


}
