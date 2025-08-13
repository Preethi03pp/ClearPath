package com.example.rbpt.init;

import com.example.rbpt.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
  private final UserService users;
  public DataInitializer(UserService users) { this.users = users; }

  @Override
  public void run(String... args) {
    if (users.findByEmail("admin@local").isEmpty()) {
      users.create("Admin", "admin@local", "admin123", "ADMIN");
      System.out.println("Initialized default admin: admin@local / admin123");
    }
  }
}
