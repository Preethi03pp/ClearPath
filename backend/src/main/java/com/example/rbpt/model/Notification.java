package com.example.rbpt.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document("notifications")
public class Notification {
  @Id public String id;
  public String userId;
  public String type;
  public String message;
  public boolean read = false;
  public String link;
  public Instant createdAt = Instant.now();
}
