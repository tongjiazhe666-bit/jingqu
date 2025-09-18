package com.example.jingqu.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "favorites")
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "scenic_spot_id", nullable = false)
    private Long scenicSpotId;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    // 构造方法
    public Favorite() {
    }

    public Favorite(Long userId, Long scenicSpotId) {
        this.userId = userId;
        this.scenicSpotId = scenicSpotId;
        this.createTime = LocalDateTime.now();
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getScenicSpotId() {
        return scenicSpotId;
    }

    public void setScenicSpotId(Long scenicSpotId) {
        this.scenicSpotId = scenicSpotId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
