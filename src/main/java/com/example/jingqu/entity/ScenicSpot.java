package com.example.jingqu.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "scenic_spot")
public class ScenicSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "image", nullable = false, length = 500)
    private String image;

    @Column(name = "tags", length = 200)
    private String tags;

    @Column(name = "is_recommended")
    private Boolean isRecommended = false;

    @Column(name = "introduction", columnDefinition = "TEXT")
    private String introduction;

    @Column(name = "open_time", length = 200)
    private String openTime;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "place", length = 500)
    private String place;

    @Column(name = "count")
    private Double count;

    @Column(name = "price")
    private Double price;

    @Column(name = "status")
    private Boolean status = true;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    // 构造方法
    public ScenicSpot() {
    }

    public ScenicSpot(String title, String image, String tags, Boolean isRecommended, String introduction, String openTime) {
        this.title = title;
        this.image = image;
        this.tags = tags;
        this.isRecommended = isRecommended;
        this.introduction = introduction;
        this.openTime = openTime;
        this.createTime = LocalDateTime.now();
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Boolean getIsRecommended() {
        return isRecommended;
    }

    public void setIsRecommended(Boolean isRecommended) {
        this.isRecommended = isRecommended;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
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

    // 辅助方法：获取标签数组
    public String[] getTagArray() {
        if (tags == null || tags.isEmpty()) {
            return new String[0];
        }
        return tags.split(",");
    }
}
