package com.example.rbpt.repo;
import com.example.rbpt.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
public interface NotificationRepository extends MongoRepository<Notification, String> {
  List<Notification> findByUserIdOrderByCreatedAtDesc(String userId);
  long countByUserIdAndReadIsFalse(String userId);
}
