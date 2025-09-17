package com.example.jingqu.repository;

import com.example.jingqu.entity.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLikeRepository extends JpaRepository<UserLike, Long> {
    
    Optional<UserLike> findByUserIdAndScenicSpotId(Long userId, Long scenicSpotId);
    
    boolean existsByUserIdAndScenicSpotId(Long userId, Long scenicSpotId);
    
    List<UserLike> findByUserIdOrderByCreateTimeDesc(Long userId);
    
    @Query("SELECT ul FROM UserLike ul WHERE ul.userId = :userId ORDER BY ul.createTime DESC")
    List<UserLike> findUserLikes(Long userId);
    
    @Query("SELECT COUNT(ul) FROM UserLike ul WHERE ul.scenicSpotId = :scenicSpotId")
    Long countByScenicSpotId(Long scenicSpotId);
    
    void deleteByUserIdAndScenicSpotId(Long userId, Long scenicSpotId);
}
