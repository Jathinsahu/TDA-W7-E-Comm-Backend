package com.ecommerce.controller;

import com.ecommerce.model.entity.Payment;
import com.ecommerce.model.entity.PaymentStatus;
import com.ecommerce.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    
    private final PaymentService paymentService;
    
    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        List<Payment> payments = paymentService.findAll();
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        return paymentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<Payment> getPaymentByOrderId(@PathVariable Long orderId) {
        return paymentService.findByOrderId(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Payment>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        List<Payment> payments = paymentService.findByStatus(status);
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/report")
    public ResponseEntity<List<Object[]>> getPaymentReport(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDateTime startDateTime = LocalDateTime.parse(startDate);
        LocalDateTime endDateTime = LocalDateTime.parse(endDate);
        List<Payment> payments = paymentService.findCompletedPaymentsBetweenDates(startDateTime, endDateTime);
        return ResponseEntity.ok(payments.stream().map(p -> new Object[]{
                p.getId(), p.getOrder().getOrderNumber(), p.getAmount(), p.getPaymentMethod(), p.getPaymentDate()
        }).toList());
    }
    
    @GetMapping("/statistics")
    public ResponseEntity<List<Object[]>> getPaymentStatistics() {
        List<Object[]> statistics = paymentService.getPaymentMethodStatistics();
        return ResponseEntity.ok(statistics);
    }
    
    @GetMapping("/revenue")
    public ResponseEntity<Double> getTotalRevenue() {
        Double revenue = paymentService.getTotalRevenue();
        return ResponseEntity.ok(revenue != null ? revenue : 0.0);
    }
    
    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Payment payment) {
        Payment savedPayment = paymentService.createPayment(payment);
        return ResponseEntity.ok(savedPayment);
    }
    
    @PutMapping("/{id}/process")
    public ResponseEntity<Payment> processPayment(@PathVariable Long id) {
        Payment processedPayment = paymentService.processPayment(id);
        return ResponseEntity.ok(processedPayment);
    }
    
    @PutMapping("/{id}/fail")
    public ResponseEntity<Payment> failPayment(@PathVariable Long id) {
        Payment failedPayment = paymentService.failPayment(id);
        return ResponseEntity.ok(failedPayment);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}