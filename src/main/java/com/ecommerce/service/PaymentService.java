package com.ecommerce.service;

import com.ecommerce.model.entity.Order;
import com.ecommerce.model.entity.Payment;
import com.ecommerce.model.entity.PaymentStatus;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {
    
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }
    
    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }
    
    public Optional<Payment> findByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }
    
    public List<Payment> findByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }
    
    public List<Payment> findCompletedPaymentsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentRepository.findCompletedPaymentsBetweenDates(startDate, endDate);
    }
    
    public Long countCompletedPayments() {
        return paymentRepository.countCompletedPayments();
    }
    
    public Double getTotalRevenue() {
        return paymentRepository.getTotalRevenue();
    }
    
    public List<Object[]> getPaymentMethodStatistics() {
        return paymentRepository.getPaymentMethodStatistics();
    }
    
    @Transactional
    public Payment createPayment(Payment payment) {
        Order order = orderRepository.findById(payment.getOrder().getId())
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        
        payment.setAmount(order.getTotalAmount());
        payment.setStatus(PaymentStatus.PENDING);
        
        Payment savedPayment = paymentRepository.save(payment);
        order.setPayment(savedPayment);
        order.setStatus(com.ecommerce.model.entity.OrderStatus.CONFIRMED);
        orderRepository.save(order);
        
        return savedPayment;
    }
    
    @Transactional
    public Payment processPayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .map(payment -> {
                    payment.setStatus(PaymentStatus.COMPLETED);
                    payment.setPaymentDate(LocalDateTime.now());
                    Payment updatedPayment = paymentRepository.save(payment);
                    
                    Order order = orderRepository.findById(payment.getOrder().getId()).orElse(null);
                    if (order != null) {
                        order.setStatus(com.ecommerce.model.entity.OrderStatus.PROCESSING);
                        orderRepository.save(order);
                    }
                    
                    return updatedPayment;
                })
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
    }
    
    @Transactional
    public Payment failPayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .map(payment -> {
                    payment.setStatus(PaymentStatus.FAILED);
                    Payment updatedPayment = paymentRepository.save(payment);
                    
                    Order order = orderRepository.findById(payment.getOrder().getId()).orElse(null);
                    if (order != null) {
                        order.setStatus(com.ecommerce.model.entity.OrderStatus.CANCELLED);
                        orderRepository.save(order);
                    }
                    
                    return updatedPayment;
                })
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
    }
    
    @Transactional
    public void deleteById(Long id) {
        paymentRepository.deleteById(id);
    }
}