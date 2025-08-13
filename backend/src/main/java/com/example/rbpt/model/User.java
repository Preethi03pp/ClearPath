package com.example.rbpt.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document("users")
public class User {
  @Id public String id;
  public String name;
  @Indexed(unique = true) public String email;
  public String password;
  public String role;
  public Instant createdAt = Instant.now();
}
