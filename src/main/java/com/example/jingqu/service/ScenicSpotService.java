package com.example.jingqu.service;

import com.example.jingqu.entity.ScenicSpot;
import com.example.jingqu.repository.ScenicSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScenicSpotService {
    
    @Autowired
    private ScenicSpotRepository scenicSpotRepository;
    
    public List<ScenicSpot> getActiveScenicSpots() {
        return scenicSpotRepository.findActiveScenicSpots();
    }
    
    public List<ScenicSpot> getRecommendedScenicSpots() {
        return scenicSpotRepository.findRecommendedScenicSpots();
    }
    
    public List<ScenicSpot> getAllScenicSpots() {
        return scenicSpotRepository.findAll();
    }
    
    public ScenicSpot getScenicSpotById(Long id) {
        return scenicSpotRepository.findById(id).orElse(null);
    }
    
    public ScenicSpot saveScenicSpot(ScenicSpot scenicSpot) {
        return scenicSpotRepository.save(scenicSpot);
    }
    
    public void deleteScenicSpot(Long id) {
        scenicSpotRepository.deleteById(id);
    }
    
    public List<ScenicSpot> getScenicSpotsByStatus(Boolean status) {
        return scenicSpotRepository.findByStatusOrderByCreateTimeDesc(status);
    }
    
    public List<ScenicSpot> getRecommendedScenicSpotsByStatus(Boolean isRecommended, Boolean status) {
        return scenicSpotRepository.findByIsRecommendedAndStatusOrderByCreateTimeDesc(isRecommended, status);
    }
}
