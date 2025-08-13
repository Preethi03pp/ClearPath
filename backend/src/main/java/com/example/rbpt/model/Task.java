package com.example.rbpt.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document("tasks")
public class Task {
  @Id public String id;
  public String projectId;
  public String title;
  public String description;
  public String status = "TODO";
  public String priority = "MEDIUM";
  public String assigneeId;
  public Instant dueDate;
  public Instant createdAt = Instant.now();
  public Instant updatedAt = Instant.now();
  public List<FileMeta> attachments = new ArrayList<>();
}
