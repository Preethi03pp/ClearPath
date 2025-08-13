package com.example.rbpt.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document("projects")
public class Project {
  @Id public String id;
  public String name;
  public String description;
  public String managerId;
  public String teamId;
  public String status = "ACTIVE";
  public Instant startDate;
  public Instant endDate;
  public Instant createdAt = Instant.now();
}
