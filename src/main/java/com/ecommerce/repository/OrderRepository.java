package com.ecommerce.repository;

import com.ecommerce.model.entity.Order;
import com.ecommerce.model.entity.OrderStatus;
import com.ecommerce.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    Page<Order> findByUser(User user, Pageable pageable);
    
    List<Order> findByStatus(OrderStatus status);
    
    Optional<Order> findByOrderNumber(String orderNumber);
    
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId ORDER BY o.createdAt DESC")
    Page<Order> findOrdersByUserId(@Param("userId") Long userId, Pageable pageable);
    
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate")
    List<Order> findOrdersBetweenDates(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.user WHERE o.orderNumber = :orderNumber")
    Optional<Order> findByOrderNumberWithUser(@Param("orderNumber") String orderNumber);
    
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.id = :id")
    Optional<Order> findByIdWithOrderItems(@Param("id") Long id);
    
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.user LEFT JOIN FETCH o.orderItems WHERE o.id = :id")
    Optional<Order> findByIdWithUserAndItems(@Param("id") Long id);
    
    @Query("SELECT o FROM Order o WHERE o.status IN :statuses")
    List<Order> findByStatusIn(@Param("statuses") List<OrderStatus> statuses);
    
    @Query(value = """
        SELECT DATE(o.created_at) as order_date,
               COUNT(*) as total_orders,
               SUM(o.total_amount) as total_revenue
        FROM orders o
        WHERE o.created_at >= :startDate
        GROUP BY DATE(o.created_at)
        ORDER BY order_date DESC
        """, nativeQuery = true)
    List<Object[]> getDailyOrderReport(@Param("startDate") LocalDateTime startDate);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.user.id = :userId")
    Long countOrdersByUserId(@Param("userId") Long userId);
    
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.user.id = :userId AND o.status = 'DELIVERED'")
    Double getTotalSpentByUserId(@Param("userId") Long userId);
}