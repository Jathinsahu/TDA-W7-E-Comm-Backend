package com.ecommerce.service;

import com.ecommerce.model.entity.*;
import com.ecommerce.repository.OrderItemRepository;
import com.ecommerce.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserService userService;
    private final ProductService productService;
    private final PaymentService paymentService;
    
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
    
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }
    
    public Optional<Order> findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }
    
    public Page<Order> findByUserId(Long userId, Pageable pageable) {
        return orderRepository.findOrdersByUserId(userId, pageable);
    }
    
    public List<Order> findByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }
    
    public List<Order> findOrdersBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findOrdersBetweenDates(startDate, endDate);
    }
    
    public Optional<Order> findByIdWithOrderItems(Long id) {
        return orderRepository.findByIdWithOrderItems(id);
    }
    
    public Optional<Order> findByIdWithUserAndItems(Long id) {
        return orderRepository.findByIdWithUserAndItems(id);
    }
    
    public List<Object[]> getDailyOrderReport(LocalDateTime startDate) {
        return orderRepository.getDailyOrderReport(startDate);
    }
    
    public Long countOrdersByUserId(Long userId) {
        return orderRepository.countOrdersByUserId(userId);
    }
    
    public Double getTotalSpentByUserId(Long userId) {
        return orderRepository.getTotalSpentByUserId(userId);
    }
    
    @Transactional
    public Order createOrder(Order order) {
        // Generate unique order number
        String orderNumber = generateOrderNumber();
        order.setOrderNumber(orderNumber);
        order.setStatus(OrderStatus.PENDING);
        
        // Process order items
        order.getOrderItems().forEach(orderItem -> {
            // Verify product stock
            if (!productService.isProductInStock(orderItem.getProduct().getId(), orderItem.getQuantity())) {
                throw new IllegalArgumentException("Insufficient stock for product: " + orderItem.getProduct().getName());
            }
            
            // Set current price and calculate subtotal
            Product product = productService.findById(orderItem.getProduct().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found"));
            orderItem.setPrice(product.getPrice());
            orderItem.calculateSubtotal();
            
            // Decrease product stock
            productService.decreaseStock(product.getId(), orderItem.getQuantity());
        });
        
        // Calculate total amount
        order.calculateTotal();
        
        // Save order
        Order savedOrder = orderRepository.save(order);
        
        return savedOrder;
    }
    
    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    order.setStatus(status);
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }
    
    @Transactional
    public Order cancelOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(order -> {
                    if (!order.canBeCancelled()) {
                        throw new IllegalStateException("Order cannot be cancelled in current status: " + order.getStatus());
                    }
                    
                    order.setStatus(OrderStatus.CANCELLED);
                    
                    // Restore product stock
                    order.getOrderItems().forEach(orderItem -> {
                        productService.increaseStock(orderItem.getProduct().getId(), orderItem.getQuantity());
                    });
                    
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }
    
    @Transactional
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }
    
    private String generateOrderNumber() {
        return "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}