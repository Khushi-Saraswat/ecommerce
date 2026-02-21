package com.example.demo.controller;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.request.Order.OrderRequestDTO;
import com.example.demo.service.methods.OrderService;

//order lifecycle, transactional stock deduction, invoices.
@RestController
@RequestMapping("/api/orders")
public class OrderController {

     @Autowired
     private OrderService orderService;

     // place order
     @PostMapping("/")
     public ResponseEntity<?> placeOrder(@RequestBody OrderRequestDTO orderRequestDto) {

          return ResponseEntity.ok(orderService.saveOrder(orderRequestDto));
     }

     // Customer Order History
     @GetMapping("/history")
     public ResponseEntity<?> getOrderHistory(
               @RequestParam(defaultValue = "0") int page,
               @RequestParam(defaultValue = "10") int size) {

          return ResponseEntity
                    .ok(orderService.getOrdersByUser(PageRequest.of(page, size, Sort.by("createdAt").descending())));
     }

     // Artisan Dashboard
     @GetMapping("/artisans/orders")
     public ResponseEntity<?> getArtisanOrders(
               @RequestParam(defaultValue = "0") int page,
               @RequestParam(defaultValue = "10") int size) {

          return ResponseEntity
                    .ok(orderService.getOrdersByArtisan(PageRequest.of(page, size, Sort.by("createdAt").descending())));
     }

     // Artisan Dashboard
     /*
      * @GetMapping("/artisan/orders/{id}/status")
      * public ResponseEntity<?> OrdersUpdated(@RequestHeader("Authorization") String
      * jwt,
      * 
      * @PathVariable("id") int artisanOrderId,
      * 
      * @RequestBody String status) {
      * 
      * return ResponseEntity.ok(orderService.updatOrderStatus(jwt, artisanOrderId,
      * status));
      * }
      */

     @GetMapping("/{orderId}/pay")
     public ResponseEntity<?> cancelOrder(@RequestParam Long orderId) throws BadRequestException {
          return ResponseEntity.ok(orderService.cancelOrder(orderId));
     }

     @GetMapping("/users/orders")
     public ResponseEntity<?> getOrderById(@RequestParam Long orderId) {

          return ResponseEntity.ok(orderService.getOrderById(orderId));

     }

}
