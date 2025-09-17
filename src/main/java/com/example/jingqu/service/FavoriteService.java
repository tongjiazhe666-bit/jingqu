package com.example.jingqu.service;

import com.example.jingqu.entity.Favorite;
import com.example.jingqu.repository.FavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    
    @Autowired
    private FavoriteRepository favoriteRepository;
    
    public List<Favorite> getUserFavorites(Long userId) {
        return favoriteRepository.findUserFavoritesOrderByCreateTimeDesc(userId);
    }
    
    public Favorite addFavorite(Long userId, Long projectId) {
        Favorite favorite = new Favorite(userId, projectId);
        return favoriteRepository.save(favorite);
    }
    
    public void removeFavorite(Long userId, Long projectId) {
        favoriteRepository.deleteByUserIdAndProjectId(userId, projectId);
    }
    
    public boolean isFavorite(Long userId, Long projectId) {
        return favoriteRepository.existsByUserIdAndProjectId(userId, projectId);
    }
    
    public long getUserFavoriteCount(Long userId) {
        return favoriteRepository.countByUserId(userId);
    }
    
    public long getProjectFavoriteCount(Long projectId) {
        return favoriteRepository.countByProjectId(projectId);
    }
    
    public Favorite getFavorite(Long userId, Long projectId) {
        return favoriteRepository.findByUserIdAndProjectId(userId, projectId);
    }
}
