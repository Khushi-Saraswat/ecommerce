package com.example.demo.service;

import java.time.LocalDate;
//import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.constants.PaymentType;
import com.example.demo.dto.OrderDto;
import com.example.demo.model.Cart;
import com.example.demo.model.OrderAddress;
//import com.example.demo.model.OrderRequest;
//import com.example.demo.model.OrderAddress;
import com.example.demo.model.ProductOrder;
import com.example.demo.model.UserDtls;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductOrderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.methods.CartService;
import com.example.demo.service.methods.OrderService;
import com.example.demo.util.CommonUtil;

@Service
public class OrderServiceImpl implements OrderService {

   @Autowired
   private CommonUtil commonUtil;

   @Autowired
   private ProductOrderRepository orderRepository;

   @Autowired
   private CartRepository cartRepository;

   @Autowired
   private UserRepository userRepository;

   @Autowired
   private CartService cartService;

   @Override
   public void saveOrder(UserDtls userdtls, OrderDto orderDto) throws Exception {
      List<Cart> carts = cartRepository.findByUserId(userdtls.getId());

      double totalitem = 0, finalPrice = 0;

      if (carts.size() == 0) {
         // return not found error

      }
      for (Cart cart : carts) {

         cart.setTotalOrderPrice(cart.getProduct().getPrice() * cart.getQuantity());
         totalitem += cart.getTotalOrderPrice();

      }

      double delievery = 100;
      double tax = totalitem * 0.18;

      finalPrice = totalitem + delievery + tax;
      // implement stock functionality also

      // create 1 order for all product in cart
      ProductOrder order = new ProductOrder();
      order.setOrderId(UUID.randomUUID().toString());
      order.setOrderDate(LocalDate.now());
      order.setPrice(finalPrice);
      order.setDelievery(delievery);
      order.setTax(tax);
      order.setUser(userdtls);

      OrderAddress orderAddress = null;
      for (Cart cart : carts) {
         orderAddress = new OrderAddress();
         orderAddress.setPaymentType(PaymentType.valueOf(orderDto.getPaymentType()));
         orderAddress.setProduct(cart.getProduct());
      }

      orderAddress.setLastName(orderDto.getLastName());
      orderAddress.setFirstName(orderDto.getFirstName());
      orderAddress.setMobileNumber(orderDto.getMobileNumber());
      orderAddress.setCity(orderAddress.getCity());

      order.setOrderAddress(orderAddress);

      orderRepository.save(order);

      cartRepository.deleteByUserId(userdtls.getId());

   }

   @Override
   public List<ProductOrder> getOrdersByUser(Long userId) {
      List<ProductOrder> orders = orderRepository.findByUserId(userId);
      return orders;

   }

   @Override
   public ProductOrder updatOrderStatus(Integer id, String st) {
      Optional<ProductOrder> findById = orderRepository.findById(id);
      if (findById.isPresent()) {
         ProductOrder productOrder = findById.get();
         productOrder.setStatus(st);
         ProductOrder updateOrder = orderRepository.save(productOrder);
         return updateOrder;
      }
      return null;
   }

   @Override
   public List<ProductOrder> getAllOrders() {
      return orderRepository.findAll();
   }

   @Override
   public ProductOrder getOrdersByOrderId(String orderId) {
      return orderRepository.findByOrderId(orderId);

   }

   @Override
   public Page<ProductOrder> getAllActiveOrderPagination(Integer pageNo, Integer PageSize) {
      Pageable page = PageRequest.of(pageNo, PageSize);
      return orderRepository.findAll(page);
   }

}
