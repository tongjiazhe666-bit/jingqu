package com.example.jingqu.controller;

import com.example.jingqu.common.ApiResponse;
import com.example.jingqu.entity.Banner;
import com.example.jingqu.entity.ScenicSpot;
import com.example.jingqu.service.BannerService;
import com.example.jingqu.service.ScenicSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private BannerService bannerService;
    
    @Autowired
    private ScenicSpotService scenicSpotService;
    
    @GetMapping("/getBanner")
    public ApiResponse<Map<String, Object>> getBanner() {
        try {
            List<Banner> banners = bannerService.getActiveBanners();
            List<Map<String, String>> bannerList = new ArrayList<>();
            
            for (Banner banner : banners) {
                Map<String, String> bannerMap = new HashMap<>();
                bannerMap.put("image", banner.getImage());
                bannerMap.put("title", banner.getTitle());
                bannerList.add(bannerMap);
            }
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("bannerList", bannerList);
            
            return ApiResponse.success(responseData);
            
        } catch (Exception e) {
            return ApiResponse.error("获取轮播图失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/getHomeList")
    public ApiResponse<List<Map<String, Object>>> getHomeList() {
        try {
            List<ScenicSpot> scenicSpots = scenicSpotService.getActiveScenicSpots();
            List<Map<String, Object>> homeList = new ArrayList<>();
            
            for (ScenicSpot spot : scenicSpots) {
                Map<String, Object> spotMap = new HashMap<>();
                spotMap.put("id", spot.getId());
                spotMap.put("title", spot.getTitle());
                spotMap.put("img", spot.getImage());
                
                // 处理标签数组
                String[] tags = spot.getTags() != null ? spot.getTags().split(",") : new String[0];
                spotMap.put("tag", tags);
                
                spotMap.put("isDot", spot.getIsRecommended() ? "推荐" : "");
                spotMap.put("dot", spot.getIsRecommended());
                spotMap.put("introduce", spot.getIntroduction());
                spotMap.put("times", spot.getOpenTime());
                spotMap.put("isPlay", false); // 默认未游玩

                // 处理地址坐标 - 使用默认坐标，因为经纬度字段已移除
                List<String> address = new ArrayList<>();
                address.add("116.410886");
                address.add("39.881949");
                spotMap.put("address", address);

                // 添加place、count和price字段
                spotMap.put("place", spot.getPlace() != null ? spot.getPlace() : "");
                spotMap.put("count", spot.getCount() != null ? spot.getCount() : 0.0);
                spotMap.put("price", spot.getPrice() != null ? spot.getPrice() : 0.0);

                homeList.add(spotMap);
            }
            
            return ApiResponse.success(homeList);
            
        } catch (Exception e) {
            return ApiResponse.error("获取首页列表失败: " + e.getMessage());
        }
    }
}
