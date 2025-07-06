package com.englishtest.englishtest.controller;

import com.englishtest.englishtest.entity.Notification;
import com.englishtest.englishtest.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    private final NotificationRepository notificationRepository;

    @PostMapping("/send-notification")
    public ResponseEntity<String> sendNotification(
            @RequestParam Long userId,
            @RequestParam String message
    ) {
        messagingTemplate.convertAndSendToUser(
                String.valueOf(userId),
                "/queue/notifications",
                message
        );
        return ResponseEntity.ok("Đã gửi noti tới userId: " + userId);
    }

    @GetMapping("/notifications")
    public ResponseEntity<?> getUnseenNotifications(@RequestParam Long userId) {
        return ResponseEntity.ok(notificationRepository.findByUserIdAndReadFalse(userId));
    }

    @PostMapping("/notifications/read")
    public ResponseEntity<?> markAsRead(@RequestParam Long notiId) {
        Notification noti = notificationRepository.findById(notiId)
                .orElseThrow(() -> new RuntimeException("Not found"));

        noti.setRead(true);
        notificationRepository.save(noti);

        return ResponseEntity.ok("Đã đánh dấu là đã đọc");
    }


}

