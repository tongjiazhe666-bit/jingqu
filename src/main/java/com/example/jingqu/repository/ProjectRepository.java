package com.example.jingqu.repository;

import com.example.jingqu.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByScenicSpotIdAndStatusOrderByCreateTimeDesc(Long scenicSpotId, Boolean status);
    
    List<Project> findByStatusOrderByCreateTimeDesc(Boolean status);
    
    @Query("SELECT p FROM Project p WHERE p.scenicSpotId = :scenicSpotId AND p.status = true ORDER BY p.createTime DESC")
    List<Project> findActiveProjectsByScenicSpotId(Long scenicSpotId);
    
    @Query("SELECT p FROM Project p WHERE p.status = true ORDER BY p.createTime DESC")
    List<Project> findActiveProjects();
    
    @Query("SELECT p FROM Project p WHERE p.status = true AND p.id != :excludeId AND p.tag LIKE %:tag% ORDER BY p.createTime DESC")
    List<Project> findSimilarProjects(Long excludeId, String tag);
}
