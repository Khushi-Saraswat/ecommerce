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

import com.example.demo.model.Cart;
import com.example.demo.model.OrderAddress;
import com.example.demo.model.OrderRequest;
//import com.example.demo.model.OrderRequest;
//import com.example.demo.model.OrderAddress;
import com.example.demo.model.ProductOrder;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductOrderRepository;
import com.example.demo.service.methods.OrderService;
import com.example.demo.util.CommonUtil;
import com.example.demo.util.OrderStatus;

@Service
public class OrderServiceImpl implements OrderService {

   @Autowired
   private CommonUtil commonUtil;

   @Autowired
   private ProductOrderRepository orderRepository;

   @Autowired
   private CartRepository cartRepository;

   @Override
   public void saveOrder(Integer userid, OrderRequest orderRequest) throws Exception {
      // List<Cart> carts = cartRepository.findByUserId(userid);
      for (Cart cart : carts) {
         ProductOrder order = new ProductOrder();
         // Random generate of id....
         order.setOrderId(UUID.randomUUID().toString());
         order.setOrderDate(LocalDate.now());
         order.setProduct(cart.getProduct());
         order.setPrice(cart.getProduct().getDiscountPrice());
         order.setQuantity(cart.getQuantity());
         order.setUser(cart.getUser());
         order.setStatus(OrderStatus.IN_PROGRESS.name());
         order.setPaymentType(orderRequest.getPaymentType());
         OrderAddress orderAddress = new OrderAddress();
         orderAddress.setFirstName(orderRequest.getFirstName());
         System.out.println(orderAddress.getFirstName() + "firstname");
         orderAddress.setLastName(orderRequest.getLastName());
         // System.out.println(orderAddress.getLastName() + "lastname");
         orderAddress.setEmail(orderRequest.getEmail());
         // System.out.println(orderAddress.getLastName() + "lastname");
         // orderAddress.setMobileNo(orderRequest.getMobileNo());
         orderAddress.setMobileNumber(orderRequest.getMobileNumber());
         System.out.println(orderRequest.getMobileNumber() + "mobile number");
         orderAddress.setAddress(orderRequest.getAddress());
         orderAddress.setCity(orderRequest.getCity());
         orderAddress.setState(orderRequest.getState());
         orderAddress.setPincode(orderRequest.getPincode());
         order.setOrderAddress(orderAddress);
         System.out.println(order.getOrderAddress());
         ProductOrder saveOrder = orderRepository.save(order);
         commonUtil.sendMailForProductOrder(saveOrder, "success");
      }

   }

   @Override
   public List<ProductOrder> getOrdersByUser(Integer userId) {
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
