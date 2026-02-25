package com.ecommerce.repository;

import com.ecommerce.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    Optional<Category> findByName(String name);
    
    List<Category> findByIsActiveTrue();
    
    List<Category> findByParentIsNullAndIsActiveTrue();
    
    List<Category> findByParentAndIsActiveTrue(Category parent);
    
    @Query("SELECT c FROM Category c WHERE c.parent IS NULL AND c.isActive = true")
    List<Category> findRootCategories();
    
    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId AND c.isActive = true")
    List<Category> findSubCategoriesByParentId(@Param("parentId") Long parentId);
    
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.subCategories WHERE c.id = :id")
    Optional<Category> findByIdWithSubCategories(@Param("id") Long id);
}