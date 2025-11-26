package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.OrderDto;
import com.example.demo.model.ProductOrder;
import com.example.demo.model.UserDtls;
import com.example.demo.service.methods.OrderService;
import com.example.demo.service.methods.UserService;
import com.example.demo.util.OrderStatus;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    public ResponseEntity<String> placeOrder(@RequestHeader("Authorization") String token,
            @RequestBody OrderDto orderDto) throws Exception {

        UserDtls userDtls = userService.UserByToken(token);
        orderService.saveOrder(userDtls, orderDto);

        return ResponseEntity.ok("h");

    }

    @GetMapping("/user")
    public ResponseEntity<List<ProductOrder>> GetOrder(@RequestHeader("Authorization") String token) {

        UserDtls userDtls = userService.UserByToken(token);
        List<ProductOrder> productOrder = orderService.getOrdersByUser(userDtls.getId());

        return ResponseEntity.ok(productOrder);

    }

    @PostMapping("/update-order-status")
    public String updateOrderStatus(@RequestParam Integer id, @RequestParam Integer st) {
        OrderStatus[] values = OrderStatus.values();
        String status = null;
        for (OrderStatus orderSt : values) {
            if (orderSt.getId().equals(st)) {
                System.out.println(orderSt.getId());
                status = orderSt.getName();
            }
        }
        ProductOrder updateOrder = orderService.updatOrderStatus(id, status);
        try {
            // commonUtil.sendMailForProductOrder(updateOrder, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!ObjectUtils.isEmpty(updateOrder)) {

            System.out.println("Status updated !!!!");
        } else {

            System.out.println("Status not updated !!!!!");
        }

        System.out.println(values + "values");
        return "jj";
    }

}
