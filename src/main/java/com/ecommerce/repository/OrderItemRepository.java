package com.ecommerce.repository;

import com.ecommerce.model.entity.Order;
import com.ecommerce.model.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    List<OrderItem> findByOrder(Order order);
    
    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId")
    List<OrderItem> findByOrderId(@Param("orderId") Long orderId);
    
    @Query("SELECT oi.product.id, SUM(oi.quantity) as totalQuantity " +
           "FROM OrderItem oi " +
           "WHERE oi.order.status = 'DELIVERED' " +
           "GROUP BY oi.product.id " +
           "ORDER BY totalQuantity DESC")
    List<Object[]> findBestSellingProducts();
    
    @Query("SELECT oi.product.category.id, SUM(oi.quantity) as totalQuantity " +
           "FROM OrderItem oi " +
           "WHERE oi.order.status = 'DELIVERED' " +
           "GROUP BY oi.product.category.id " +
           "ORDER BY totalQuantity DESC")
    List<Object[]> findBestSellingCategories();
}