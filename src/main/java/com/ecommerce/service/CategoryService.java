package com.ecommerce.service;

import com.ecommerce.model.entity.Category;
import com.ecommerce.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    
    private final CategoryRepository categoryRepository;
    
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
    
    public List<Category> findAllActive() {
        return categoryRepository.findByIsActiveTrue();
    }
    
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }
    
    public Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }
    
    public List<Category> findRootCategories() {
        return categoryRepository.findRootCategories();
    }
    
    public List<Category> findSubCategories(Long parentId) {
        return categoryRepository.findSubCategoriesByParentId(parentId);
    }
    
    public Optional<Category> findByIdWithSubCategories(Long id) {
        return categoryRepository.findByIdWithSubCategories(id);
    }
    
    @Transactional
    public Category save(Category category) {
        return categoryRepository.save(category);
    }
    
    @Transactional
    public Category update(Category category) {
        return categoryRepository.save(category);
    }
    
    @Transactional
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
    
    @Transactional
    public void deactivateCategory(Long id) {
        categoryRepository.findById(id).ifPresent(category -> {
            category.setActive(false);
            categoryRepository.save(category);
        });
    }
}