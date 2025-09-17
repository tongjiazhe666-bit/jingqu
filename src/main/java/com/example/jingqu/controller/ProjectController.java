package com.example.jingqu.controller;

import com.example.jingqu.common.ApiResponse;
import com.example.jingqu.entity.Project;
import com.example.jingqu.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    
    @Autowired
    private ProjectService projectService;
    
    @PostMapping("/info")
    public ApiResponse<Map<String, Object>> getProjectInfo(@RequestBody Map<String, Long> request) {
        try {
            Long id = request.get("id");
            if (id == null) {
                return ApiResponse.error("项目ID不能为空");
            }
            
            Project project = projectService.getProjectById(id);
            if (project == null) {
                return ApiResponse.error("项目不存在");
            }
            
            Map<String, Object> projectInfo = new HashMap<>();
            projectInfo.put("id", project.getId());
            projectInfo.put("scenicSpot_Id", project.getScenicSpotId());
            projectInfo.put("title", project.getTitle());
            projectInfo.put("content", project.getDescription());
            
            // 模拟图片数组
            String[] images = {project.getImage(), project.getImage()};
            projectInfo.put("images", images);
            
            // 模拟价格和时长
            projectInfo.put("price", 100.00);
            projectInfo.put("duration", "2小时");
            
            // 添加经纬度位置信息
            if (project.getLatitude() != null && project.getLongitude() != null) {
                Double[] location = {project.getLongitude().doubleValue(), project.getLatitude().doubleValue()};
                projectInfo.put("location", location);
            }
            
            // 添加相似项目数组
            List<Map<String, Object>> similarProjects = new java.util.ArrayList<>();
            if (project.getTag() != null && !project.getTag().trim().isEmpty()) {
                List<Project> similarList = projectService.getSimilarProjects(id, project.getTag());
                for (Project similar : similarList) {
                    Map<String, Object> similarProject = new HashMap<>();
                    similarProject.put("title", similar.getTitle());
                    similarProject.put("image", similar.getImage());
                    similarProjects.add(similarProject);
                }
            }
            projectInfo.put("similiar", similarProjects);
            
            return ApiResponse.success(projectInfo);
            
        } catch (Exception e) {
            return ApiResponse.error("获取项目详情失败: " + e.getMessage());
        }
    }
}
