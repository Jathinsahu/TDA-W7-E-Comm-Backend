package com.ecommerce.repository;

import com.ecommerce.model.entity.Category;
import com.ecommerce.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Page<Product> findByIsActiveTrue(Pageable pageable);
    
    Page<Product> findByCategoryAndIsActiveTrue(Category category, Pageable pageable);
    
    List<Product> findByIsActiveTrueAndStockGreaterThan(Integer stock);
    
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.name LIKE %:keyword%")
    Page<Product> findActiveProductsByNameContaining(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.price BETWEEN :minPrice AND :maxPrice")
    Page<Product> findActiveProductsByPriceRange(@Param("minPrice") BigDecimal minPrice, 
                                                @Param("maxPrice") BigDecimal maxPrice, 
                                                Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.category.id = :categoryId")
    Page<Product> findActiveProductsByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);
    
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.id = :id AND p.isActive = true")
    Product findActiveProductByIdWithCategory(@Param("id") Long id);
    
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.stock <= :threshold ORDER BY p.stock ASC")
    List<Product> findLowStockProducts(@Param("threshold") Integer threshold);
    
    @Query(value = """
        SELECT p.id, p.name, p.price, p.stock, c.name as category_name
        FROM products p
        JOIN categories c ON p.category_id = c.id
        WHERE p.is_active = true
        AND (:categoryId IS NULL OR p.category_id = :categoryId)
        AND (:minPrice IS NULL OR p.price >= :minPrice)
        AND (:maxPrice IS NULL OR p.price <= :maxPrice)
        ORDER BY p.created_at DESC
        """, nativeQuery = true)
    List<Object[]> findProductsWithFilters(@Param("categoryId") Long categoryId,
                                          @Param("minPrice") BigDecimal minPrice,
                                          @Param("maxPrice") BigDecimal maxPrice);
}