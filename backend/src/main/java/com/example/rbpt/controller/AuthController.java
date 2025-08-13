package com.example.rbpt.controller;

import com.example.rbpt.model.User;
import com.example.rbpt.security.JwtUtil;
import com.example.rbpt.service.SecurityService;
import com.example.rbpt.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthController {
  private final UserService users;
  private final PasswordEncoder encoder;
  private final JwtUtil jwt;
  private final SecurityService sec;

  public AuthController(UserService users, PasswordEncoder encoder, JwtUtil jwt, SecurityService sec) {
    this.users = users; 
    this.encoder = encoder; 
    this.jwt = jwt; 
    this.sec = sec;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
    String name = body.getOrDefault("name", "User");
    String email = body.get("email");
    String password = body.get("password");
    if (email == null || password == null) {
      return ResponseEntity.badRequest().body(Map.of("error", "email & password required"));
    }
    if (users.findByEmail(email).isPresent()) {
      return ResponseEntity.badRequest().body(Map.of("error", "email already exists"));
    }
    User u = users.create(name, email, password, "MEMBER");
    String token = jwt.generateToken(u.email, Map.of("id", u.id, "role", u.role, "name", u.name));
    return ResponseEntity.ok(Map.of("token", token, "user", u));
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
    String email = body.get("email");
    String password = body.get("password");
    Optional<User> userOpt = users.findByEmail(email);
    if (!userOpt.isPresent() || !encoder.matches(password, userOpt.get().password)) {
      return ResponseEntity.status(401).body(Map.of("error", "invalid credentials"));
    }
    User u = userOpt.get();
    String token = jwt.generateToken(u.email, Map.of("id", u.id, "role", u.role, "name", u.name));
    return ResponseEntity.ok(Map.of("token", token, "user", u));
  }

  @GetMapping("/me")
  public ResponseEntity<?> me() {
    User u = sec.currentUser();
    return u == null ? ResponseEntity.status(401).build() : ResponseEntity.ok(u);
  }
}
