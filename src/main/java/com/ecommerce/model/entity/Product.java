package com.ecommerce.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "products", 
       indexes = {
           @Index(name = "idx_product_name", columnList = "name"),
           @Index(name = "idx_product_category", columnList = "category_id"),
           @Index(name = "idx_product_price", columnList = "price")
       })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(nullable = false)
    private Integer stock;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    @Column(name = "image_url", length = 500)
    private String imageUrl;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    public void setActive(Boolean active) {
        this.isActive = active;
    }
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @CreatedBy
    @Column(name = "created_by")
    private String createdBy;
    
    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;
    
    // Business methods
    public boolean isInStock() {
        return stock > 0 && isActive;
    }
    
    public boolean isInStock(Integer quantity) {
        return stock >= quantity && isActive;
    }
    
    public void decreaseStock(Integer quantity) {
        if (quantity > stock) {
            throw new IllegalArgumentException("Insufficient stock: requested " + quantity + ", available " + stock);
        }
        this.stock -= quantity;
    }
    
    public void increaseStock(Integer quantity) {
        this.stock += quantity;
    }
}