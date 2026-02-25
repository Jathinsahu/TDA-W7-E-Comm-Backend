package com.ecommerce.service;

import com.ecommerce.model.entity.Category;
import com.ecommerce.model.entity.Product;
import com.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    
    public Page<Product> findAllActive(Pageable pageable) {
        return productRepository.findByIsActiveTrue(pageable);
    }
    
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }
    
    public Product findActiveProductByIdWithCategory(Long id) {
        return productRepository.findActiveProductByIdWithCategory(id);
    }
    
    public Page<Product> findByCategory(Long categoryId, Pageable pageable) {
        Optional<Category> category = categoryService.findById(categoryId);
        return category.map(value -> productRepository.findByCategoryAndIsActiveTrue(value, pageable))
                      .orElse(Page.empty());
    }
    
    public Page<Product> searchByName(String keyword, Pageable pageable) {
        return productRepository.findActiveProductsByNameContaining(keyword, pageable);
    }
    
    public Page<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.findActiveProductsByPriceRange(minPrice, maxPrice, pageable);
    }
    
    public List<Product> findLowStockProducts(Integer threshold) {
        return productRepository.findLowStockProducts(threshold);
    }
    
    public List<Object[]> findProductsWithFilters(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findProductsWithFilters(categoryId, minPrice, maxPrice);
    }
    
    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }
    
    @Transactional
    public Product update(Product product) {
        return productRepository.save(product);
    }
    
    @Transactional
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
    
    @Transactional
    public void deactivateProduct(Long id) {
        productRepository.findById(id).ifPresent(product -> {
            product.setActive(false);
            productRepository.save(product);
        });
    }
    
    @Transactional
    public void updateStock(Long productId, Integer quantity) {
        productRepository.findById(productId).ifPresent(product -> {
            product.setStock(quantity);
            productRepository.save(product);
        });
    }
    
    @Transactional
    public void decreaseStock(Long productId, Integer quantity) {
        productRepository.findById(productId).ifPresent(product -> {
            product.decreaseStock(quantity);
            productRepository.save(product);
        });
    }
    
    @Transactional
    public void increaseStock(Long productId, Integer quantity) {
        productRepository.findById(productId).ifPresent(product -> {
            product.increaseStock(quantity);
            productRepository.save(product);
        });
    }
    
    public boolean isProductInStock(Long productId, Integer quantity) {
        return productRepository.findById(productId)
                .map(product -> product.isInStock(quantity))
                .orElse(false);
    }
}