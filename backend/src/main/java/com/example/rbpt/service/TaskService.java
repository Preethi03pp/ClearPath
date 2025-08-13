package com.example.rbpt.service;

import com.example.rbpt.model.Task;
import com.example.rbpt.repo.TaskRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
  private final TaskRepository repo;
  private final NotificationService notifications;

  public TaskService(TaskRepository repo, NotificationService notifications) {
    this.repo = repo; this.notifications = notifications;
  }

  public Task create(Task t) {
    t.createdAt = Instant.now(); t.updatedAt = Instant.now();
    Task saved = repo.save(t);
    if (t.assigneeId != null) {
      notifications.push(t.assigneeId, "TASK_ASSIGNED", "New task assigned: " + t.title, "/tasks");
    }
    return saved;
  }

  public Task update(Task updated) {
    Optional<Task> existingOpt = repo.findById(updated.id);
    if (existingOpt.isEmpty()) return null;
    Task existing = existingOpt.get();
    String prevAssignee = existing.assigneeId;
    String prevStatus = existing.status;

    updated.updatedAt = Instant.now();
    Task saved = repo.save(updated);

    if (saved.assigneeId != null && (prevAssignee == null || !prevAssignee.equals(saved.assigneeId))) {
      notifications.push(saved.assigneeId, "TASK_ASSIGNED", "Task assigned: " + saved.title, "/tasks");
    }
    if (!prevStatus.equals(saved.status) && saved.assigneeId != null) {
      notifications.push(saved.assigneeId, "TASK_UPDATED", "Task status updated: " + saved.title + " -> " + saved.status, "/tasks");
    }
    return saved;
  }

  public Optional<Task> get(String id) { return repo.findById(id); }
  public List<Task> all() { return repo.findAll(); }
  public void delete(String id) { repo.deleteById(id); }
  public List<Task> byAssignee(String uid) { return repo.findByAssigneeId(uid); }
  public List<Task> byProject(String pid) { return repo.findByProjectId(pid); }
}
