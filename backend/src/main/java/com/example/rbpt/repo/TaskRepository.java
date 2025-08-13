package com.example.rbpt.repo;
import com.example.rbpt.model.Task;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
public interface TaskRepository extends MongoRepository<Task, String> {
  List<Task> findByAssigneeId(String assigneeId);
  List<Task> findByProjectId(String projectId);
}
