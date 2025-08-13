package com.example.rbpt.controller;

import com.example.rbpt.model.User;
import com.example.rbpt.service.SecurityService;
import com.example.rbpt.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {
  private final UserService users;
  private final SecurityService sec;

  public UserController(UserService users, SecurityService sec) {
    this.users = users; this.sec = sec;
  }

  @GetMapping
  public ResponseEntity<?> list() {
    if (!sec.isAdmin()) return ResponseEntity.status(403).build();
    return ResponseEntity.ok(users.all());
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody Map<String, String> body) {
    if (!sec.isAdmin()) return ResponseEntity.status(403).build();
    String name = body.getOrDefault("name", "User");
    String email = body.get("email");
    String password = body.getOrDefault("password", "changeme123");
    String role = body.getOrDefault("role", "MEMBER");
    User u = users.create(name, email, password, role);
    return ResponseEntity.ok(u);
  }

  @PatchMapping("/{id}/role")
  public ResponseEntity<?> setRole(@PathVariable String id, @RequestBody Map<String, String> body) {
    if (!sec.isAdmin()) return ResponseEntity.status(403).build();
    String role = body.get("role");
    return users.get(id).map(u -> {
      u.role = role; return ResponseEntity.ok(users.save(u));
    }).orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable String id) {
    if (!sec.isAdmin()) return ResponseEntity.status(403).build();
    users.delete(id);
    return ResponseEntity.noContent().build();
  }
}
