package com.example.jingqu.service;

import com.example.jingqu.entity.UserLike;
import com.example.jingqu.repository.UserLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserLikeService {
    
    @Autowired
    private UserLikeRepository userLikeRepository;
    
    @Transactional
    public boolean toggleLike(Long userId, Long scenicSpotId) {
        boolean exists = userLikeRepository.existsByUserIdAndScenicSpotId(userId, scenicSpotId);
        
        if (exists) {
            userLikeRepository.deleteByUserIdAndScenicSpotId(userId, scenicSpotId);
            return false;
        } else {
            UserLike userLike = new UserLike(userId, scenicSpotId);
            userLikeRepository.save(userLike);
            return true;
        }
    }
    
    public boolean isLiked(Long userId, Long scenicSpotId) {
        return userLikeRepository.existsByUserIdAndScenicSpotId(userId, scenicSpotId);
    }
    
    public List<UserLike> getUserLikes(Long userId) {
        return userLikeRepository.findByUserIdOrderByCreateTimeDesc(userId);
    }
    
    public Long getLikeCount(Long scenicSpotId) {
        return userLikeRepository.countByScenicSpotId(scenicSpotId);
    }
    
    @Transactional
    public void deleteByUserIdAndScenicSpotId(Long userId, Long scenicSpotId) {
        userLikeRepository.deleteByUserIdAndScenicSpotId(userId, scenicSpotId);
    }
}
