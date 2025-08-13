package com.example.rbpt.controller;

import com.example.rbpt.model.Notification;
import com.example.rbpt.model.User;
import com.example.rbpt.service.NotificationService;
import com.example.rbpt.service.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin
public class NotificationController {
  private final NotificationService service;
  private final SecurityService sec;

  public NotificationController(NotificationService service, SecurityService sec) { 
    this.service = service; 
    this.sec = sec; 
  }

  @GetMapping
  public List<Notification> list() {
    User u = sec.currentUser();
    return service.forUser(u.id);
  }

  @GetMapping("/unread-count")
  public Map<String, Long> unreadCount() {
    User u = sec.currentUser();
    return Map.of("count", service.unreadCount(u.id));
  }

  @PostMapping("/{id}/read")
  public ResponseEntity<?> markRead(@PathVariable String id) {
    User u = sec.currentUser();
    Notification n = service.markRead(id);
    if (n == null || !n.userId.equals(u.id)) {
      return ResponseEntity.status(404).build();
    }
    return ResponseEntity.ok(n);
  }
}
