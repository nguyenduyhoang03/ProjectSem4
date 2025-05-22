package com.TrainingSouls.Controller;

import com.TrainingSouls.DTO.Request.NotificationContent;
import com.TrainingSouls.DTO.Request.NotificationMessage;
import com.TrainingSouls.Entity.Notification;
import com.TrainingSouls.Service.FirebaseMessagingService;
import com.TrainingSouls.Service.NotificationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final FirebaseMessagingService firebaseMessagingService;


    @PostMapping("saveFcmToken")
    public void saveFcmToken(HttpServletRequest request, @RequestParam String fcmToken) {
        firebaseMessagingService.saveToken(request, fcmToken);
    }

    @PostMapping("/send-to-user/{userId}")
    public String sendNotificationToUser(@PathVariable Long userId, @RequestBody NotificationContent content) {
        return firebaseMessagingService.sendNotificationToUser(userId, content);
    }


    @PostMapping("/app")
    public String sendNotificationByToken(@RequestBody NotificationMessage notification) {
        return firebaseMessagingService.sendNotificationByToken(notification);
    }

    @GetMapping("/getNotifications")
    public List<Notification> getNotifications(HttpServletRequest req) {
        return notificationService.getNotifications(req);
    }

    @PatchMapping("/read/{id}")
    public void read(@PathVariable long id) {
        notificationService.markAsRead(id);
    }

    @PostMapping("/notifyCoachLevelTest/{dateTime}")
    public void notifyCoachLevelTest(HttpServletRequest request,@PathVariable LocalDateTime dateTime) {
        notificationService.notifyCoachLevelTestRequest(request, dateTime);
    }
}
