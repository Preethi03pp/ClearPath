package com.example.rbpt.controller;

import com.example.rbpt.model.Project;
import com.example.rbpt.service.ProjectService;
import com.example.rbpt.service.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin
public class ProjectController {
  private final ProjectService service;
  private final SecurityService sec;

  public ProjectController(ProjectService service, SecurityService sec) {
    this.service = service;
    this.sec = sec;
  }

  @GetMapping
  public ResponseEntity<?> list(@RequestParam(required = false) String managerId,
                                @RequestParam(required = false) String teamId,
                                @RequestParam(required = false) String status) {
    List<Project> all = service.all();
    if (managerId != null) all = all.stream().filter(p -> managerId.equals(p.managerId)).toList();
    if (teamId != null) all = all.stream().filter(p -> teamId.equals(p.teamId)).toList();
    if (status != null) all = all.stream().filter(p -> status.equalsIgnoreCase(p.status)).toList();
    return ResponseEntity.ok(all);
  }

  @GetMapping("/{id}")
public ResponseEntity<?> getById(@PathVariable String id) {
  Optional<Project> project = service.get(id);
  if (project.isEmpty()) return ResponseEntity.notFound().build();

  Project p = project.get();

  // Security check: if user is manager, allow only if they own the project
  if (sec.isManager() && !p.managerId.equals(sec.currentUser().id)) {
    return ResponseEntity.status(403).build();
  }

  return ResponseEntity.ok(p);
}

  @PostMapping
public ResponseEntity<?> create(@RequestBody Project p) {
  System.out.println("Create project request by user: " + sec.currentUser());
  System.out.println("Is Admin? " + sec.isAdmin());
  System.out.println("Is Manager? " + sec.isManager());
  if (!(sec.isAdmin() || sec.isManager())) {
    System.out.println("Forbidden: user lacks permission");
    return ResponseEntity.status(403).build();
  }
  if (sec.isManager()) p.managerId = sec.currentUser().id;
  return ResponseEntity.ok(service.save(p));
}


  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable String id, @RequestBody Project p) {
    if (!(sec.isAdmin() || sec.isManager())) return ResponseEntity.status(403).build();
    Optional<Project> existing = service.get(id);
    if (existing.isEmpty()) return ResponseEntity.notFound().build();
    Project e = existing.get();
    if (sec.isManager() && !e.managerId.equals(sec.currentUser().id)) return ResponseEntity.status(403).build();
    p.id = id;
    if (p.managerId == null) p.managerId = e.managerId;
    return ResponseEntity.ok(service.save(p));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable String id) {
    if (!(sec.isAdmin() || sec.isManager())) return ResponseEntity.status(403).build();
    Optional<Project> existing = service.get(id);
    if (existing.isEmpty()) return ResponseEntity.notFound().build();
    Project e = existing.get();
    if (sec.isManager() && !e.managerId.equals(sec.currentUser().id)) return ResponseEntity.status(403).build();
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
