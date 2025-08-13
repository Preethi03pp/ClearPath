package com.example.rbpt.controller;

import com.example.rbpt.model.Team;
import com.example.rbpt.repo.TeamRepository;
import com.example.rbpt.service.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/teams")
@CrossOrigin
public class TeamController {
  private final TeamRepository repo;
  private final SecurityService sec;

  public TeamController(TeamRepository repo, SecurityService sec) { this.repo = repo; this.sec = sec; }

  @GetMapping public List<Team> all() { return repo.findAll(); }

  @PostMapping public ResponseEntity<?> create(@RequestBody Team t) {
    if (!(sec.isAdmin() || sec.isManager())) return ResponseEntity.status(403).build();
    return ResponseEntity.ok(repo.save(t));
  }

  @PutMapping("/{id}") public ResponseEntity<?> update(@PathVariable String id, @RequestBody Team t) {
    if (!(sec.isAdmin() || sec.isManager())) return ResponseEntity.status(403).build();
    t.id = id; return ResponseEntity.ok(repo.save(t));
  }

  @DeleteMapping("/{id}") public ResponseEntity<?> delete(@PathVariable String id) {
    if (!(sec.isAdmin() || sec.isManager())) return ResponseEntity.status(403).build();
    repo.deleteById(id); return ResponseEntity.noContent().build();
  }
}
