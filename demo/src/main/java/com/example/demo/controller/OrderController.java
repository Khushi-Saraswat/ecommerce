package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.methods.OrderService;

//order lifecycle, transactional stock deduction, invoices.
@RestController
@RequestMapping("/api/orders")
public class OrderController {

     @Autowired
     private OrderService orderService;

     // place order
     @PostMapping("/")
     public ResponseEntity<?> placeOrder(@RequestHeader("Authorization") String jwt, @RequestParam Integer addressId,
               @RequestParam String paymentMethod) {

          return ResponseEntity.ok(orderService.saveOrder(jwt, addressId, paymentMethod));
     }

     // Customer Order History
     @GetMapping("/history")
     public ResponseEntity<?> getOrderHistory(@RequestHeader("Authorization") String jwt) {

          return ResponseEntity.ok(orderService.getOrdersByUser(jwt));
     }

     // Artisan Dashboard
     @GetMapping("/artisans/orders")
     public ResponseEntity<?> getArtisanOrders(@RequestHeader("Authorization") String jwt) {

          return ResponseEntity.ok(orderService.getOrdersByArtisan(jwt));
     }

     // Artisan Dashboard
     @GetMapping("/artisan/orders/{id}/status")
     public ResponseEntity<?> OrdersUpdated(@RequestHeader("Authorization") String jwt,
               @PathVariable("id") int artisanOrderId,
               @RequestBody String status) {

          return ResponseEntity.ok(orderService.updatOrderStatus(jwt, artisanOrderId, status));
     }

     @GetMapping("/{orderId}/pay")
     public ResponseEntity<?> cancelOrder(@RequestParam String orderId) {
          return ResponseEntity.ok(orderService.cancelOrder(orderId));
     }

}
