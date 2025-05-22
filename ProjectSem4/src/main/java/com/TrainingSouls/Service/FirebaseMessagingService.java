package com.TrainingSouls.Service;

import com.TrainingSouls.DTO.Request.NotificationContent;
import com.TrainingSouls.DTO.Request.NotificationMessage;
import com.TrainingSouls.Entity.DeviceToken;
import com.TrainingSouls.Repository.DeviceTokenRepository;
import com.TrainingSouls.Utils.JWTUtils;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FirebaseMessagingService {
    private final FirebaseMessaging firebaseMessaging;
    private final DeviceTokenRepository deviceTokenRepository;

    public String sendNotificationByToken(NotificationMessage notificationMessage) {
        Notification notification = Notification.builder()
                .setTitle(notificationMessage.getTitle())
                .setBody(notificationMessage.getBody())
                .setImage(notificationMessage.getImage())
                .build();

        Message.Builder messageBuilder = Message.builder()
                .setToken(notificationMessage.getRecipientToken())
                .setNotification(notification);

        // Nếu có data thì mới thêm
        if (notificationMessage.getData() != null) {
            messageBuilder.putAllData(notificationMessage.getData());
        }

        try {
            firebaseMessaging.send(messageBuilder.build());
            return "gui thong bao thanh cong";
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return "error";
        }
    }


    public void saveToken(HttpServletRequest request, String token) {
        long userId = JWTUtils.getSubjectFromRequest(request);
        Optional<DeviceToken> existing = deviceTokenRepository.findByUserId(userId);
        if (existing.isPresent()) {
            existing.get().setFcmToken(token);
            existing.get().setUpdatedAt(LocalDateTime.now());
            deviceTokenRepository.save(existing.get());
        } else {
            deviceTokenRepository.save(DeviceToken.builder()
                    .userId(userId)
                    .fcmToken(token)
                    .updatedAt(LocalDateTime.now())
                    .build()
            );
        }
    }

    public String sendNotificationToUser(Long userId, NotificationContent notificationContent) {
        String token = deviceTokenRepository.findByUserId(userId).orElseThrow(()-> new RuntimeException("khong tim thay token")).getFcmToken();

        NotificationMessage msg = new NotificationMessage();
        msg.setRecipientToken(token);
        msg.setTitle(notificationContent.getTitle());
        msg.setBody(notificationContent.getBody());
        msg.setImage(notificationContent.getImage());
        msg.setData(notificationContent.getData());

        return sendNotificationByToken(msg);
    }

}
