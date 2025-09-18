package com.example.jingqu.service;

import com.example.jingqu.entity.Favorite;
import com.example.jingqu.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FavoriteService {
    
    @Autowired
    private FavoriteRepository favoriteRepository;
    
    public List<Favorite> getUserFavorites(Long userId) {
        return favoriteRepository.findUserFavoritesOrderByCreateTimeDesc(userId);
    }
    
    public Favorite addFavorite(Long userId, Long scenicSpotId) {
        Favorite favorite = new Favorite(userId, scenicSpotId);
        return favoriteRepository.save(favorite);
    }
    
    @Transactional
    public void removeFavorite(Long userId, Long scenicSpotId) {
        favoriteRepository.deleteByUserIdAndScenicSpotId(userId, scenicSpotId);
    }
    
    public boolean isFavorite(Long userId, Long scenicSpotId) {
        return favoriteRepository.existsByUserIdAndScenicSpotId(userId, scenicSpotId);
    }
    
    public long getUserFavoriteCount(Long userId) {
        return favoriteRepository.countByUserId(userId);
    }
    
    public long getScenicSpotFavoriteCount(Long scenicSpotId) {
        return favoriteRepository.countByScenicSpotId(scenicSpotId);
    }
    
    public Favorite getFavorite(Long userId, Long scenicSpotId) {
        return favoriteRepository.findByUserIdAndScenicSpotId(userId, scenicSpotId);
    }
}
