package com.ecommerce.repository;

import com.ecommerce.model.entity.Order;
import com.ecommerce.model.entity.Payment;
import com.ecommerce.model.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
    Optional<Payment> findByOrder(Order order);
    
    List<Payment> findByStatus(PaymentStatus status);
    
    @Query("SELECT p FROM Payment p WHERE p.order.id = :orderId")
    Optional<Payment> findByOrderId(@Param("orderId") Long orderId);
    
    @Query("SELECT p FROM Payment p WHERE p.status = 'COMPLETED' AND p.paymentDate BETWEEN :startDate AND :endDate")
    List<Payment> findCompletedPaymentsBetweenDates(@Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.status = 'COMPLETED'")
    Long countCompletedPayments();
    
    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.status = 'COMPLETED'")
    Double getTotalRevenue();
    
    @Query("SELECT p.paymentMethod, COUNT(p) as count, SUM(p.amount) as total " +
           "FROM Payment p WHERE p.status = 'COMPLETED' " +
           "GROUP BY p.paymentMethod " +
           "ORDER BY total DESC")
    List<Object[]> getPaymentMethodStatistics();
}