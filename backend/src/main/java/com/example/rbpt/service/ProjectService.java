package com.example.rbpt.service;

import com.example.rbpt.model.Project;
import com.example.rbpt.repo.ProjectRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
  private final ProjectRepository repo;
  public ProjectService(ProjectRepository repo) { this.repo = repo; }

  public Project save(Project p) { return repo.save(p); }
  public Optional<Project> get(String id) { return repo.findById(id); }
  public List<Project> all() { return repo.findAll(); }
  public void delete(String id) { repo.deleteById(id); }
  public List<Project> byManager(String managerId) { return repo.findByManagerId(managerId); }
  public List<Project> byTeam(String teamId) { return repo.findByTeamId(teamId); }
}
