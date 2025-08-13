package com.example.rbpt.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document("teams")
public class Team {
  @Id public String id;
  public String name;
  public List<String> memberIds = new ArrayList<>();
}
