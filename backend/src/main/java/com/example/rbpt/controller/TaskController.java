package com.example.rbpt.controller;

import com.example.rbpt.model.Task;
import com.example.rbpt.model.User;
import com.example.rbpt.service.SecurityService;
import com.example.rbpt.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin
public class TaskController {
  private final TaskService service;
  private final SecurityService sec;

  public TaskController(TaskService service, SecurityService sec) {
    this.service = service;
    this.sec = sec;
  }

  @GetMapping
  public ResponseEntity<?> list(@RequestParam(required = false) String projectId,
                                @RequestParam(required = false) String assigneeId,
                                @RequestParam(required = false) String status,
                                @RequestParam(required = false) String priority,
                                @RequestParam(required = false) String q) {
    List<Task> all = service.all();
    if (projectId != null) all = all.stream().filter(t -> projectId.equals(t.projectId)).toList();
    if (assigneeId != null) all = all.stream().filter(t -> assigneeId.equals(t.assigneeId)).toList();
    if (status != null) all = all.stream().filter(t -> status.equalsIgnoreCase(t.status)).toList();
    if (priority != null) all = all.stream().filter(t -> priority.equalsIgnoreCase(t.priority)).toList();
    if (q != null && !q.isBlank()) {
      String qq = q.toLowerCase();
      all = all.stream().filter(t -> (t.title != null && t.title.toLowerCase().contains(qq))
          || (t.description != null && t.description.toLowerCase().contains(qq))).toList();
    }
    return ResponseEntity.ok(all);
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody Task t) {
    if (!(sec.isAdmin() || sec.isManager())) return ResponseEntity.status(403).build();
    return ResponseEntity.ok(service.create(t));
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> update(@PathVariable String id, @RequestBody Task t) {
    Optional<Task> existing = service.get(id);
    if (existing.isEmpty()) return ResponseEntity.notFound().build();
    Task e = existing.get();
    User user = sec.currentUser();
    boolean canEdit = sec.isAdmin() || sec.isManager() || (sec.isMember() && user.id.equals(e.assigneeId));
    if (!canEdit) return ResponseEntity.status(403).build();
    t.id = id;
    return ResponseEntity.ok(service.update(t));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable String id) {
    if (!(sec.isAdmin() || sec.isManager())) return ResponseEntity.status(403).build();
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
