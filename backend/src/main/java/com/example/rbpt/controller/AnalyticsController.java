package com.example.rbpt.controller;

import com.example.rbpt.model.Project;
import com.example.rbpt.model.Task;
import com.example.rbpt.model.User;
import com.example.rbpt.repo.ProjectRepository;
import com.example.rbpt.repo.TaskRepository;
import com.example.rbpt.repo.UserRepository;
import com.example.rbpt.service.SecurityService;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin
public class AnalyticsController {

    private final ProjectRepository projects;
    private final TaskRepository tasks;
    private final UserRepository users;
    private final SecurityService sec;

    public AnalyticsController(ProjectRepository projects, TaskRepository tasks, UserRepository users, SecurityService sec) {
        this.projects = projects;
        this.tasks = tasks;
        this.users = users;
        this.sec = sec;
    }

    @GetMapping
    public Map<String, Object> overview() {
        User user = sec.currentUser();
        List<Project> pList = projects.findAll();
        List<Task> tList = tasks.findAll();

        if ("MANAGER".equals(user.role)) {
            pList = pList.stream()
                    .filter(p -> user.id.equals(p.managerId))
                    .collect(Collectors.toList());

            Set<String> managerProjectIds = pList.stream()
                    .map(p -> p.id)
                    .collect(Collectors.toSet());

            tList = tList.stream()
                    .filter(t -> managerProjectIds.contains(t.projectId))
                    .collect(Collectors.toList());

        } else if ("MEMBER".equals(user.role)) {
            tList = tList.stream()
                    .filter(t -> user.id.equals(t.assigneeId))
                    .collect(Collectors.toList());

            Set<String> memberProjectIds = tList.stream()
                    .map(t -> t.projectId)
                    .collect(Collectors.toSet());

            pList = pList.stream()
                    .filter(p -> memberProjectIds.contains(p.id))
                    .collect(Collectors.toList());
        }

        long totalProjects = pList.size();
        long totalTasks = tList.size();

        Map<String, Long> tasksByStatus = tList.stream()
                .collect(Collectors.groupingBy(t -> t.status, Collectors.counting()));

        Map<String, Long> tasksByPriority = tList.stream()
                .collect(Collectors.groupingBy(t -> t.priority, Collectors.counting()));

        Map<String, Long> tasksPerAssignee = tList.stream()
                .filter(t -> t.assigneeId != null)
                .collect(Collectors.groupingBy(t -> t.assigneeId, Collectors.counting()));

        Map<String, Object> result = new HashMap<>();
        result.put("totalProjects", totalProjects);
        result.put("totalTasks", totalTasks);
        result.put("tasksByStatus", tasksByStatus);
        result.put("tasksByPriority", tasksByPriority);
        result.put("tasksPerAssignee", tasksPerAssignee);

        return result;
    }
}
