package com.example.jingqu.controller;

import com.example.jingqu.common.ApiResponse;
import com.example.jingqu.entity.Favorite;
import com.example.jingqu.entity.ScenicSpot;
import com.example.jingqu.service.FavoriteService;
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
    private FavoriteService favoriteService;
    
    @Autowired
    private ScenicSpotService scenicSpotService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @GetMapping("/list")
    public ApiResponse<Map<String, Object>> getLikeList(@RequestHeader("token") String token) {
        try {
            if (!jwtUtil.validateToken(token)) {
                return ApiResponse.unauthorized("Token无效");
            }
            
            Long userId = jwtUtil.getUserIdFromToken(token);
            List<Map<String, Object>> likeList = new ArrayList<>();
            
            // 获取用户收藏的景区ID列表（使用新的favorites表）
            favoriteService.getUserFavorites(userId).forEach(favorite -> {
                ScenicSpot scenicSpot = scenicSpotService.getScenicSpotById(favorite.getScenicSpotId());
                if (scenicSpot != null && scenicSpot.getStatus()) {
                    Map<String, Object> spotMap = new HashMap<>();
                    spotMap.put("id", scenicSpot.getId());
                    spotMap.put("title", scenicSpot.getTitle());
                    spotMap.put("img", scenicSpot.getImage());
                    spotMap.put("introduce", scenicSpot.getIntroduction());
                    spotMap.put("isDot", scenicSpot.getIsRecommended() ? "推荐" : "");
                    spotMap.put("createTime", favorite.getCreateTime());
                    
                    likeList.add(spotMap);
                }
            });
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("jingqu", likeList);
            
            return ApiResponse.success(responseData);
            
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
    
    @PostMapping("/gettag")
    public ApiResponse<Map<String, Object>> getLikeTag(@RequestHeader("token") String token,
                                                     @RequestBody Map<String, Long> request) {
        try {
            if (!jwtUtil.validateToken(token)) {
                return ApiResponse.unauthorized("Token无效");
            }
            
            Long userId = jwtUtil.getUserIdFromToken(token);
            Long scenicSpotId = request.get("id");
            
            if (scenicSpotId == null) {
                return ApiResponse.error("景区ID不能为空");
            }
            
            // 使用FavoriteService检查是否存在收藏关系
            boolean isFavorite = favoriteService.isFavorite(userId, scenicSpotId);
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("tag", isFavorite ? 1 : 0);
            
            return ApiResponse.success(responseData);
            
        } catch (Exception e) {
            return ApiResponse.error("获取喜欢标签状态失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/changetag")
    public ApiResponse<Map<String, Object>> changeLikeTag(@RequestHeader("token") String token,
                                                        @RequestBody Map<String, Long> request) {
        try {
            if (!jwtUtil.validateToken(token)) {
                return ApiResponse.unauthorized("Token无效");
            }
            
            Long userId = jwtUtil.getUserIdFromToken(token);
            Long scenicSpotId = request.get("id");
            
            if (scenicSpotId == null) {
                return ApiResponse.error("景区ID不能为空");
            }
            
            // 使用FavoriteService切换收藏状态
            boolean isFavorite = favoriteService.isFavorite(userId, scenicSpotId);
            
            if (isFavorite) {
                // 如果已收藏，则取消收藏（删除记录）
                favoriteService.removeFavorite(userId, scenicSpotId);
            } else {
                // 如果未收藏，则添加收藏（插入记录）
                favoriteService.addFavorite(userId, scenicSpotId);
            }
            
            // 返回切换后的状态
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("tag", !isFavorite ? 1 : 0);
            
            return ApiResponse.success(responseData);
            
        } catch (Exception e) {
            return ApiResponse.error("切换喜欢标签状态失败: " + e.getMessage());
        }
    }
}
