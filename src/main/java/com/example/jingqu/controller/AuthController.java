package com.example.jingqu.controller;

import com.example.jingqu.common.ApiResponse;
import com.example.jingqu.entity.User;
import com.example.jingqu.service.UserService;
import com.example.jingqu.service.WechatService;
import com.example.jingqu.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private WechatService wechatService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/login")
    public ApiResponse<Map<String, Object>> login(@RequestBody Map<String, Object> request) {
        try {
            String code = (String) request.get("code");
            
            // 调用微信API获取真实的openid
            String openid = wechatService.getOpenid(code);
            
            if (openid == null) {
                return ApiResponse.error("微信登录失败，请检查code是否有效");
            }
            
            // 获取用户信息（支持两种调用方式）
            String nickName = "微信用户";
            String avatarUrl = "https://thirdwx.qlogo.cn/mmopen/vi_32/POgEwh4mIHO4nibH0KlMECNjjGxQUq24ZEaGT4poC6icRiccVGKSyXwibcPq4BWmiaIGuG1icwxaQX6grC9VemZoJ8rg/132";
            String email = null;
            String phone = null;
            
            // 检查是否有用户信息更新
            if (request.containsKey("userInfo")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> userInfoMap = (Map<String, Object>) request.get("userInfo");
                if (userInfoMap.containsKey("nickName")) {
                    nickName = (String) userInfoMap.get("nickName");
                }
                if (userInfoMap.containsKey("avatarUrl")) {
                    avatarUrl = (String) userInfoMap.get("avatarUrl");
                }
                if (userInfoMap.containsKey("email")) {
                    email = (String) userInfoMap.get("email");
                }
                if (userInfoMap.containsKey("phone")) {
                    phone = (String) userInfoMap.get("phone");
                }
            }
            
            // 检查用户是否已存在
            User existingUser = userService.getUserByOpenid(openid);
            int flag;
            String token = null;
            Map<String, Object> userInfo = new HashMap<>();
            
            boolean hasUserInfo = request.containsKey("userInfo");
            
            if (existingUser != null) {
                // 用户已存在
                flag = 1;
                
                if (hasUserInfo) {
                    // 更新用户信息
                    if (email != null) {
                        existingUser.setEmail(email);
                    }
                    if (phone != null) {
                        existingUser.setPhone(phone);
                    }
                    existingUser = userService.updateUser(existingUser);
                }
                
                // 生成JWT token
                token = jwtUtil.generateToken(existingUser.getId());
                
                // 构建用户信息
                userInfo.put("nickName", existingUser.getNickName());
                userInfo.put("avatarUrl", existingUser.getAvatarUrl());
                userInfo.put("email", existingUser.getEmail());
                userInfo.put("phone", existingUser.getPhone());
            } else {
                // 用户不存在
                if (hasUserInfo) {
                    // 创建新用户
                    User newUser = new User(openid, nickName, avatarUrl);
                    if (email != null) {
                        newUser.setEmail(email);
                    }
                    if (phone != null) {
                        newUser.setPhone(phone);
                    }
                    existingUser = userService.updateUser(newUser);
                    
                    flag = 1;
                    token = jwtUtil.generateToken(existingUser.getId());
                    userInfo.put("nickName", existingUser.getNickName());
                    userInfo.put("avatarUrl", existingUser.getAvatarUrl());
                    userInfo.put("email", existingUser.getEmail());
                    userInfo.put("phone", existingUser.getPhone());
                } else {
                    // 用户不存在且没有提供userInfo
                    flag = 2;
                    // token和userInfo保持为空
                }
            }
            
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", token);
            responseData.put("userInfo", userInfo.isEmpty() ? null : userInfo);
            responseData.put("flag", flag);
            
            return ApiResponse.success(responseData);
            
        } catch (Exception e) {
            return ApiResponse.error("登录失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/getUserInfo")
    public ApiResponse<Map<String, Object>> getUserInfo(@RequestHeader("token") String token) {
        try {
            if (!jwtUtil.validateToken(token)) {
                return ApiResponse.unauthorized("Token无效");
            }
            
            Long userId = jwtUtil.getUserIdFromToken(token);
            User user = userService.getUserById(userId);
            
            if (user == null) {
                return ApiResponse.error("用户不存在");
            }
            
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("openid", user.getOpenid());
            userInfo.put("nickName", user.getNickName());
            userInfo.put("avatarUrl", user.getAvatarUrl());
            userInfo.put("email", user.getEmail());
            userInfo.put("phone", user.getPhone());
            userInfo.put("createTime", user.getCreateTime());
            userInfo.put("updateTime", user.getUpdateTime());

            return ApiResponse.success(userInfo);
            
        } catch (Exception e) {
            return ApiResponse.error("获取用户信息失败: " + e.getMessage());
        }
    }
}
