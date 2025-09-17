package com.example.jingqu.controller;

import com.example.jingqu.common.ApiResponse;
import com.example.jingqu.entity.Favorite;
import com.example.jingqu.service.FavoriteService;
import com.example.jingqu.service.ProjectService;
import com.example.jingqu.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FavoriteController {
    
    @Autowired
    private FavoriteService favoriteService;
    
    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping("/favorites")
    public ApiResponse<List<Map<String, Object>>> getUserFavorites(@RequestHeader("token") String token) {
        try {
            if (!jwtUtil.validateToken(token)) {
                return ApiResponse.unauthorized("Token无效");
            }
            
            Long userId = jwtUtil.getUserIdFromToken(token);
            List<Favorite> favorites = favoriteService.getUserFavorites(userId);
            
            List<Map<String, Object>> favoriteList = new ArrayList<>();
            for (Favorite favorite : favorites) {
                Map<String, Object> favoriteInfo = new HashMap<>();
                favoriteInfo.put("id", favorite.getId());
                favoriteInfo.put("projectId", favorite.getProjectId());
                favoriteInfo.put("createTime", favorite.getCreateTime());
                
                // 获取项目详细信息
                var project = projectService.getProjectById(favorite.getProjectId());
                if (project != null) {
                    Map<String, Object> projectInfo = new HashMap<>();
                    projectInfo.put("title", project.getTitle());
                    projectInfo.put("image", project.getImage());
                    projectInfo.put("description", project.getDescription());
                    projectInfo.put("tag", project.getTag());
                    favoriteInfo.put("projectInfo", projectInfo);
                }
                
                favoriteList.add(favoriteInfo);
            }
            
            return ApiResponse.success(favoriteList);
            
        } catch (Exception e) {
            return ApiResponse.error("获取收藏列表失败: " + e.getMessage());
        }
    }
}
