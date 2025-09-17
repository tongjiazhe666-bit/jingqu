package com.example.jingqu.service;

import com.example.jingqu.entity.Banner;
import com.example.jingqu.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerService {
    
    @Autowired
    private BannerRepository bannerRepository;
    
    public List<Banner> getActiveBanners() {
        return bannerRepository.findActiveBanners();
    }
    
    public List<Banner> getAllBanners() {
        return bannerRepository.findAll();
    }
    
    public Banner getBannerById(Long id) {
        return bannerRepository.findById(id).orElse(null);
    }
    
    public Banner saveBanner(Banner banner) {
        return bannerRepository.save(banner);
    }
    
    public void deleteBanner(Long id) {
        bannerRepository.deleteById(id);
    }
    
    public List<Banner> getBannersByStatus(Boolean status) {
        return bannerRepository.findByStatusOrderBySortAsc(status);
    }
}
