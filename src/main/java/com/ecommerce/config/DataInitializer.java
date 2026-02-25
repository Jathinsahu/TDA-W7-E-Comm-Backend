package com.ecommerce.config;

import com.ecommerce.model.entity.*;
import com.ecommerce.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Create sample users
        if (userRepository.count() == 0) {
            User user1 = User.builder()
                    .username("john_doe")
                    .email("john@example.com")
                    .password("password123")
                    .firstName("John")
                    .lastName("Doe")
                    .phone("123-456-7890")
                    .isActive(true)
                    .isVerified(true)
                    .build();
            
            User user2 = User.builder()
                    .username("jane_smith")
                    .email("jane@example.com")
                    .password("password123")
                    .firstName("Jane")
                    .lastName("Smith")
                    .phone("098-765-4321")
                    .isActive(true)
                    .isVerified(true)
                    .build();
            
            userRepository.save(user1);
            userRepository.save(user2);
        }
        
        // Create sample categories if they don't exist
        if (categoryRepository.count() == 0) {
            Category electronics = Category.builder()
                    .name("Electronics")
                    .description("Electronic devices and accessories")
                    .isActive(true)
                    .build();
            
            Category clothing = Category.builder()
                    .name("Clothing")
                    .description("Apparel and fashion items")
                    .isActive(true)
                    .build();
            
            Category books = Category.builder()
                    .name("Books")
                    .description("Books and educational materials")
                    .isActive(true)
                    .build();
            
            Category homeGarden = Category.builder()
                    .name("Home & Garden")
                    .description("Home improvement and garden supplies")
                    .isActive(true)
                    .build();
            
            categoryRepository.save(electronics);
            categoryRepository.save(clothing);
            categoryRepository.save(books);
            categoryRepository.save(homeGarden);
        }
        
        // Create sample products if they don't exist
        if (productRepository.count() == 0) {
            Category electronics = categoryRepository.findByName("Electronics").orElseThrow();
            Category clothing = categoryRepository.findByName("Clothing").orElseThrow();
            Category homeGarden = categoryRepository.findByName("Home & Garden").orElseThrow();
            
            Product iphone = Product.builder()
                    .name("iPhone 15 Pro")
                    .description("Latest Apple smartphone with advanced camera system")
                    .price(new BigDecimal("999.99"))
                    .stock(50)
                    .category(electronics)
                    .isActive(true)
                    .build();
            
            Product samsung = Product.builder()
                    .name("Samsung Galaxy S24")
                    .description("Flagship Android smartphone with AI features")
                    .price(new BigDecimal("899.99"))
                    .stock(30)
                    .category(electronics)
                    .isActive(true)
                    .build();
            
            Product macbook = Product.builder()
                    .name("MacBook Pro 14\"")
                    .description("Professional laptop for developers and creators")
                    .price(new BigDecimal("1999.99"))
                    .stock(20)
                    .category(electronics)
                    .isActive(true)
                    .build();
            
            Product tshirt = Product.builder()
                    .name("Cotton T-Shirt")
                    .description("Comfortable cotton t-shirt in various colors")
                    .price(new BigDecimal("29.99"))
                    .stock(100)
                    .category(clothing)
                    .isActive(true)
                    .build();
            
            Product jeans = Product.builder()
                    .name("Denim Jeans")
                    .description("Classic denim jeans for everyday wear")
                    .price(new BigDecimal("79.99"))
                    .stock(75)
                    .category(clothing)
                    .isActive(true)
                    .build();
            
            Product gardeningKit = Product.builder()
                    .name("Spring Gardening Kit")
                    .description("Complete gardening tools for spring planting")
                    .price(new BigDecimal("49.99"))
                    .stock(40)
                    .category(homeGarden)
                    .isActive(true)
                    .build();
            
            productRepository.save(iphone);
            productRepository.save(samsung);
            productRepository.save(macbook);
            productRepository.save(tshirt);
            productRepository.save(jeans);
            productRepository.save(gardeningKit);
        }
        
        System.out.println("✅ Data initialization completed successfully!");
        System.out.println("📊 Database statistics:");
        System.out.println("   • Users: " + userRepository.count());
        System.out.println("   • Categories: " + categoryRepository.count());
        System.out.println("   • Products: " + productRepository.count());
    }
}