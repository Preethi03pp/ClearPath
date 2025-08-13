package com.example.rbpt.repo;
import com.example.rbpt.model.Project;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
public interface ProjectRepository extends MongoRepository<Project, String> {
  List<Project> findByManagerId(String managerId);
  List<Project> findByTeamId(String teamId);
}
