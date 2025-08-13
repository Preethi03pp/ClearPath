package com.example.rbpt.controller;

import com.example.rbpt.model.FileMeta;
import com.example.rbpt.model.Task;
import com.example.rbpt.model.User;
import com.example.rbpt.service.FileStorageService;
import com.example.rbpt.service.SecurityService;
import com.example.rbpt.service.TaskService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@CrossOrigin
public class UploadController {
  private final FileStorageService files;
  private final TaskService tasks;
  private final SecurityService sec;

  public UploadController(FileStorageService files, TaskService tasks, SecurityService sec) {
    this.files = files;
    this.tasks = tasks;
    this.sec = sec;
  }

  @PostMapping("/api/tasks/{taskId}/attachments")
  public ResponseEntity<?> uploadToTask(@PathVariable String taskId, @RequestParam("file") MultipartFile file) throws IOException {
    Optional<Task> tOpt = tasks.get(taskId);
    if (tOpt.isEmpty()) return ResponseEntity.notFound().build();
    Task t = tOpt.get();
    User user = sec.currentUser();
    boolean can = sec.isAdmin() || sec.isManager() || (sec.isMember() && user.id.equals(t.assigneeId));
    if (!can) return ResponseEntity.status(403).build();

    FileMeta meta = files.save(file);
    t.attachments.add(meta);
    tasks.update(t);
    return ResponseEntity.ok(meta);
  }

  @GetMapping("/api/files/{stored}")
  public ResponseEntity<?> download(@PathVariable String stored) throws IOException {
    byte[] data = files.readFile(stored);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + stored + "\"")
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(data);
  }
}
