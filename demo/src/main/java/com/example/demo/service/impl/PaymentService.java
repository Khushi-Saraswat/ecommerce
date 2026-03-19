package com.example.demo.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constants.OrderStatus;
import com.example.demo.constants.PaymentStatus;
import com.example.demo.constants.PaymentType;
import com.example.demo.model.Order;
import com.example.demo.model.Payment;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    /**
     * Dummy payment processing
     * Replace with Razorpay/Stripe in production
     */
    public Payment processPayment(Order order, String paymentMethod) {

        PaymentType method = parsePaymentMethod(paymentMethod);

        Payment payment = Payment.builder()
                .order(order) // better than orderId
                .user(order.getUser()) // use relation
                .amount(order.getTotalAmount())
                .currency("INR")
                .method(method)
                .status(PaymentStatus.INITIATED)
                .build();

        payment = paymentRepository.save(payment);

        // Simulated logic
        if (method == PaymentType.COD) {

            payment.setStatus(PaymentStatus.SUCCESS);
            order.setPaymentStatus(PaymentStatus.PENDING);

        } else {

            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setTransactionId(generateTransactionId());

            if (method == PaymentType.CREDIT_CARD
                    || method == PaymentType.DEBIT_CARD) {

                payment.setCardLast4("4242");
                payment.setCardBrand("VISA");
            }

            order.setPaymentStatus(PaymentStatus.SUCCESS);
            order.setOrderStatus(OrderStatus.CONFIRMED);
        }

        orderRepository.save(order);

        return payment;
    }

    private PaymentType parsePaymentMethod(String method) {
        try {
            return PaymentType.valueOf(method.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PaymentType.COD;
        }
    }

    private String generateTransactionId() {
        return "TXN" + UUID.randomUUID().toString()
                .replace("-", "")
                .substring(0, 12)
                .toUpperCase();
    }
}