package com.example.jingqu.repository;

import com.example.jingqu.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    
    List<Favorite> findByUserId(Long userId);
    
    List<Favorite> findByProjectId(Long projectId);
    
    Favorite findByUserIdAndProjectId(Long userId, Long projectId);
    
    boolean existsByUserIdAndProjectId(Long userId, Long projectId);
    
    long countByUserId(Long userId);
    
    long countByProjectId(Long projectId);
    
    @Query("SELECT f FROM Favorite f WHERE f.userId = :userId ORDER BY f.createTime DESC")
    List<Favorite> findUserFavoritesOrderByCreateTimeDesc(Long userId);
    
    void deleteByUserIdAndProjectId(Long userId, Long projectId);
}
