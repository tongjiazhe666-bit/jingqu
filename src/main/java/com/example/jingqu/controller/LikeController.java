package com.example.jingqu.controller;

import com.example.jingqu.common.ApiResponse;
import com.example.jingqu.entity.ScenicSpot;
import com.example.jingqu.service.ScenicSpotService;
import com.example.jingqu.service.UserLikeService;
import com.example.jingqu.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/like")
public class LikeController {
    
    @Autowired
    private UserLikeService userLikeService;
    
    @Autowired
    private ScenicSpotService scenicSpotService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping("/list")
    public ApiResponse<List<Map<String, Object>>> getLikeList(@RequestHeader("token") String token) {
        try {
            if (!jwtUtil.validateToken(token)) {
                return ApiResponse.unauthorized("Token无效");
            }
            
            Long userId = jwtUtil.getUserIdFromToken(token);
            List<Map<String, Object>> likeList = new ArrayList<>();
            
            // 获取用户收藏的景区ID列表
            userLikeService.getUserLikes(userId).forEach(userLike -> {
                ScenicSpot scenicSpot = scenicSpotService.getScenicSpotById(userLike.getScenicSpotId());
                if (scenicSpot != null && scenicSpot.getStatus()) {
                    Map<String, Object> spotMap = new HashMap<>();
                    spotMap.put("id", scenicSpot.getId());
                    spotMap.put("title", scenicSpot.getTitle());
                    spotMap.put("img", scenicSpot.getImage());
                    spotMap.put("introduce", scenicSpot.getIntroduction());
                    spotMap.put("isDot", scenicSpot.getIsRecommended() ? "推荐" : "");
                    
                    likeList.add(spotMap);
                }
            });
            
            return ApiResponse.success(likeList);
            
        } catch (Exception e) {
            return ApiResponse.error("获取收藏列表失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/toggle")
    public ApiResponse<Map<String, Object>> toggleLike(@RequestHeader("token") String token, 
                                                     @RequestBody Map<String, Long> request) {
        try {
            if (!jwtUtil.validateToken(token)) {
                return ApiResponse.unauthorized("Token无效");
            }
            
            Long userId = jwtUtil.getUserIdFromToken(token);
            Long scenicSpotId = request.get("scenic_spot_id");
            
            if (scenicSpotId == null) {
                return ApiResponse.error("景区ID不能为空");
            }
            
            boolean liked = userLikeService.toggleLike(userId, scenicSpotId);
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("liked", liked);
            
            return ApiResponse.success(responseData);
            
        } catch (Exception e) {
            return ApiResponse.error("操作收藏失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/status")
    public ApiResponse<Map<String, Object>> checkLikeStatus(@RequestHeader("token") String token, 
                                                          @RequestBody Map<String, Long> request) {
        try {
            if (!jwtUtil.validateToken(token)) {
                return ApiResponse.unauthorized("Token无效");
            }
            
            Long userId = jwtUtil.getUserIdFromToken(token);
            Long scenicSpotId = request.get("scenic_spot_id");
            
            if (scenicSpotId == null) {
                return ApiResponse.error("景区ID不能为空");
            }
            
            boolean liked = userLikeService.isLiked(userId, scenicSpotId);
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("liked", liked);
            
            return ApiResponse.success(responseData);
            
        } catch (Exception e) {
            return ApiResponse.error("检查收藏状态失败: " + e.getMessage());
        }
    }
}
