package com.example.jingqu.service;

import com.example.jingqu.entity.User;
import com.example.jingqu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User findOrCreateUser(String openid, String nickName, String avatarUrl) {
        Optional<User> existingUser = userRepository.findByOpenid(openid);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            // 更新用户信息
            user.setNickName(nickName);
            user.setAvatarUrl(avatarUrl);
            return userRepository.save(user);
        } else {
            // 创建新用户
            User newUser = new User(openid, nickName, avatarUrl);
            return userRepository.save(newUser);
        }
    }
    
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    
    public User getUserByOpenid(String openid) {
        return userRepository.findByOpenid(openid).orElse(null);
    }
    
    public boolean userExists(String openid) {
        return userRepository.existsByOpenid(openid);
    }
    
    public User updateUser(User user) {
        return userRepository.save(user);
    }
}
