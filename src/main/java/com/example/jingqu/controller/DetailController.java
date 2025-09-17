package com.example.jingqu.controller;

import com.example.jingqu.common.ApiResponse;
import com.example.jingqu.entity.Project;
import com.example.jingqu.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/detail")
public class DetailController {
    
    @Autowired
    private ProjectService projectService;
    
    @GetMapping("/project")
    public ApiResponse<List<Map<String, Object>>> getProjects(@RequestParam(required = false) Long scenicSpotId) {
        try {
            List<Project> projects;
            if (scenicSpotId != null) {
                projects = projectService.getActiveProjectsByScenicSpotId(scenicSpotId);
            } else {
                projects = projectService.getActiveProjects();
            }
            
            List<Map<String, Object>> projectList = new ArrayList<>();
            
            for (Project project : projects) {
                Map<String, Object> projectMap = new HashMap<>();
                projectMap.put("id", project.getId());
                projectMap.put("belong", project.getScenicSpotId()); // 添加belong字段表示所属景区
                projectMap.put("title", project.getTitle());
                projectMap.put("url", project.getImage());
                projectMap.put("tag", project.getTag());
                projectMap.put("desc", project.getDescription());
                
                // 添加经纬度信息
                if (project.getLatitude() != null && project.getLongitude() != null) {
                    projectMap.put("latitude", project.getLatitude());
                    projectMap.put("longitude", project.getLongitude());
                }
                
                projectList.add(projectMap);
            }
            
            return ApiResponse.success(projectList);
            
        } catch (Exception e) {
            return ApiResponse.error("获取游玩项目失败: " + e.getMessage());
        }
    }
}
