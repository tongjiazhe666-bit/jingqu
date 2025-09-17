package com.example.jingqu.repository;

import com.example.jingqu.entity.ScenicSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScenicSpotRepository extends JpaRepository<ScenicSpot, Long> {
    List<ScenicSpot> findByStatusOrderByCreateTimeDesc(Boolean status);
    
    List<ScenicSpot> findByIsRecommendedAndStatusOrderByCreateTimeDesc(Boolean isRecommended, Boolean status);
    
    @Query("SELECT s FROM ScenicSpot s WHERE s.status = true ORDER BY s.createTime DESC")
    List<ScenicSpot> findActiveScenicSpots();
    
    @Query("SELECT s FROM ScenicSpot s WHERE s.isRecommended = true AND s.status = true ORDER BY s.createTime DESC")
    List<ScenicSpot> findRecommendedScenicSpots();
}
