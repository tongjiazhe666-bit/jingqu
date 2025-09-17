package com.example.jingqu.repository;

import com.example.jingqu.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    List<Banner> findByStatusOrderBySortAsc(Boolean status);
    
    @Query("SELECT b FROM Banner b WHERE b.status = true ORDER BY b.sort ASC")
    List<Banner> findActiveBanners();
}
