package com.example.jingqu.repository;

import com.example.jingqu.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    
    List<Favorite> findByUserId(Long userId);
    
    List<Favorite> findByScenicSpotId(Long scenicSpotId);
    
    Favorite findByUserIdAndScenicSpotId(Long userId, Long scenicSpotId);
    
    boolean existsByUserIdAndScenicSpotId(Long userId, Long scenicSpotId);
    
    long countByUserId(Long userId);
    
    long countByScenicSpotId(Long scenicSpotId);
    
    @Query("SELECT f FROM Favorite f WHERE f.userId = :userId ORDER BY f.createTime DESC")
    List<Favorite> findUserFavoritesOrderByCreateTimeDesc(Long userId);
    
    void deleteByUserIdAndScenicSpotId(Long userId, Long scenicSpotId);
}
