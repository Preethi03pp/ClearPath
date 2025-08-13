package com.example.rbpt.service;

import com.example.rbpt.model.Notification;
import com.example.rbpt.repo.NotificationRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class NotificationService {
  private final NotificationRepository repo;

  public NotificationService(NotificationRepository repo) { this.repo = repo; }

  public Notification push(String userId, String type, String message, String link) {
    Notification n = new Notification();
    n.userId = userId; n.type = type; n.message = message; n.link = link;
    return repo.save(n);
  }
  public List<Notification> forUser(String userId) { return repo.findByUserIdOrderByCreatedAtDesc(userId); }
  public long unreadCount(String userId) { return repo.countByUserIdAndReadIsFalse(userId); }
  public Notification markRead(String id) {
    return repo.findById(id).map(n -> { n.read = true; return repo.save(n); }).orElse(null);
  }
}
