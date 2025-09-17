package com.example.jingqu.service;

import com.example.jingqu.entity.Project;
import com.example.jingqu.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    
    @Autowired
    private ProjectRepository projectRepository;
    
    public List<Project> getActiveProjectsByScenicSpotId(Long scenicSpotId) {
        return projectRepository.findActiveProjectsByScenicSpotId(scenicSpotId);
    }
    
    public List<Project> getActiveProjects() {
        return projectRepository.findActiveProjects();
    }
    
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
    
    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }
    
    public Project saveProject(Project project) {
        return projectRepository.save(project);
    }
    
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
    
    public List<Project> getProjectsByScenicSpotIdAndStatus(Long scenicSpotId, Boolean status) {
        return projectRepository.findByScenicSpotIdAndStatusOrderByCreateTimeDesc(scenicSpotId, status);
    }
    
    public List<Project> getProjectsByStatus(Boolean status) {
        return projectRepository.findByStatusOrderByCreateTimeDesc(status);
    }
    
    public List<Project> getSimilarProjects(Long excludeId, String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            return List.of();
        }
        return projectRepository.findSimilarProjects(excludeId, tag);
    }
}
