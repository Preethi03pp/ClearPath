package com.example.rbpt.service;

import com.example.rbpt.model.User;
import com.example.rbpt.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
  private final UserRepository repo;
  private final PasswordEncoder encoder;

  public UserService(UserRepository repo, PasswordEncoder encoder) {
    this.repo = repo; this.encoder = encoder;
  }

  public Optional<User> findByEmail(String email) { return repo.findByEmail(email); }
  public User save(User u) { return repo.save(u); }
  public User create(String name, String email, String rawPassword, String role) {
    User u = new User();
    u.name = name; u.email = email; u.password = encoder.encode(rawPassword); u.role = role;
    return repo.save(u);
  }
  public List<User> all() { return repo.findAll(); }
  public Optional<User> get(String id) { return repo.findById(id); }
  public void delete(String id) { repo.deleteById(id); }
}
