package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.Common.AbstractMapperService;
import com.example.demo.constants.ArtisanOrderStatus;
import com.example.demo.constants.OrderStatus;
import com.example.demo.constants.errorTypes.CartErrorType;
import com.example.demo.constants.errorTypes.OrderErrorType;
import com.example.demo.exception.Cart.CartException;
import com.example.demo.exception.Order.OrderException;
import com.example.demo.model.Address;
import com.example.demo.model.Artisan;
import com.example.demo.model.ArtisanOrder;
import com.example.demo.model.Cart;
import com.example.demo.model.Order;
import com.example.demo.model.User;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.ArtisanOrderRepository;
import com.example.demo.repository.ArtisanRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.response.Order.OrderHistoryDTO;
import com.example.demo.response.User.UserResponseDTO;
import com.example.demo.service.methods.OrderService;
import com.example.demo.service.methods.UserService;
import com.razorpay.RazorpayClient;

@Service
public class OrderServiceImpl implements OrderService {

     @Autowired
     private AddressRepository addressRepository;

     @Autowired
     private CartRepository cartRepository;

     @Autowired
     private OrderRepository orderRepository;

     @Autowired
     private UserService userService;

     @Autowired
     private AbstractMapperService abstractMapperService;

     @Autowired
     private UserRepository userRepository;

     @Autowired
     private ArtisanRepository artisanRepository;

     @Autowired
     private ArtisanOrderRepository artisanOrderRepository;

     @Autowired
     private RazorpayClient razorpayClient;

     @Value("${razorpay.key_id}")
     private String razorpayKeyId;

     @Value("${razorpay.key_secret}")
     private String razorpayKeySecret;

     // place orders

     @Override
     public String saveOrder(String jwt, Integer addressId, String paymentType) {
          try {
               UserResponseDTO userDtlsDto = userService.UserByToken(jwt);
               User user = abstractMapperService.toEntity(userDtlsDto, User.class);

               List<Cart> cart = userRepository.findCartsByUserId(user.getUserId());
               if (cart == null || cart.isEmpty()) {
                    throw new CartException("Cart is empty", CartErrorType.CART_EMPTY);
               }

               int sum = 0;

               // Step 1: Validate stock & calculate total
               for (Cart c : cart) {
                    if (c.getProduct().getStock() < c.getQuantity()) {
                         throw new CartException("Stock not available for product " + c.getProduct().getName(),
                                   CartErrorType.PRODUCT_OUT_OF_STOCK);
                    }
                    sum += c.getProduct().getPrice() * c.getQuantity();
               }

               Address address = addressRepository.findById(addressId)
                         .orElseThrow(() -> new OrderException("Address not found", OrderErrorType.ADDRESS_NOT_FOUND));

               // Step 2: Create Parent Order
               Order order = new Order();
               order.setUser(user);
               order.setAddress(address);
               order.setPaymentType(paymentType);
               order.setOrderStatus(OrderStatus.PLACED);
               order.setTotalAmount(BigDecimal.valueOf(sum));

               // Step 3: Group cart by Artisan
               Map<Long, List<Cart>> map = new HashMap<>();

               for (Cart c : cart) {
                    Long artisanId = Long.valueOf(c.getArtisan().getId());
                    map.computeIfAbsent(artisanId, k -> new ArrayList<>()).add(c);
               }

               // simulate payment here for full order
               // add fake razorpay payment...

               if (paymentType.equalsIgnoreCase("online")) {
                    // create order request
                    com.razorpay.Order razorpayOrder = null;
                    try {
                         org.json.JSONObject orderRequest = new org.json.JSONObject();
                         orderRequest.put("amount", sum * 100); // amount in paise
                         orderRequest.put("currency", "INR");
                         orderRequest.put("receipt", "order_rcptid_" + order.getUser().getUsername());
                         orderRequest.put("payment_capture", 1);

                         razorpayOrder = razorpayClient.orders.create(orderRequest);
                         order.setRazorpayOrderId(razorpayOrder.get("id"));
                         order.setPaymentStatus(razorpayOrder.get("status"));
                    } catch (Exception e) {
                         throw new OrderException("Failed to create Razorpay order: " + e.getMessage(),
                                   OrderErrorType.PAYMENT_PROCESSING_FAILED);
                    }
               }
               // Step 4: Create Artisan Orders
               List<ArtisanOrder> artisanOrders = new ArrayList<>();

               for (Map.Entry<Long, List<Cart>> entry : map.entrySet()) {
                    Long artisanId = entry.getKey();
                    List<Cart> artisanCarts = entry.getValue();

                    Artisan artisan = artisanRepository.findById(artisanId)
                              .orElseThrow(() -> new OrderException("Artisan not found: " + artisanId,
                                        OrderErrorType.ORDER_CREATION_FAILED));

                    ArtisanOrder artisanOrder = new ArtisanOrder();
                    artisanOrder.setOrder(order);
                    artisanOrder.setArtisan(artisan);
                    artisanOrder.setRazorpayOrderId(order.getRazorpayOrderId());
                    artisanOrder.setPaymentStatus(order.getPaymentStatus());

                    double subtotal = 0;
                    for (Cart c : artisanCarts) {
                         subtotal += c.getProduct().getPrice() * c.getQuantity();
                    }
                    artisanOrder.setSubtotal(BigDecimal.valueOf(subtotal));

                    // Process payment through payment service layer
                    // paymentService.processPayment(artisanOrder, paymentType, subtotal, artisanId,
                    // user.getUserId(),
                    // order.getId());

                    artisanOrders.add(artisanOrder);
               }

               // fake payment perform

               order.setArtisanOrders(artisanOrders);

               // Step 5: Save Order (cascade will save artisan orders)
               Order orders = orderRepository.save(order);

               // Step 6: Clear Cart
               cartRepository.deleteAll(cart);

               if (orders == null) {
                    return "orders are not placed successfully";
               }

               return "orders placed successfully";

          } catch (CartException e) {
               throw e;
          } catch (OrderException e) {
               throw e;
          } catch (Exception e) {
               throw new OrderException("Failed to place order: " + e.getMessage(),
                         OrderErrorType.ORDER_CREATION_FAILED);
          }
     }

     @Override
     public List<OrderHistoryDTO> getOrdersByUser(String jwt) {

          try {
               UserResponseDTO userDtlsDto = userService.UserByToken(jwt);
               User user = abstractMapperService.toEntity(userDtlsDto, User.class);

               if (user == null) {
                    throw new OrderException("User not found", OrderErrorType.ORDER_NOT_FOUND);
               }

               // 1. Fetch all orders of this user
               List<Order> orders = orderRepository.findByUser_UserId(user.getUserId());

               List<OrderHistoryDTO> response = new ArrayList<>();

               // 2. For each order, build DTO
               for (Order order : orders) {

                    // Fetch artisan orders for this order
                    List<ArtisanOrder> artisanOrders = artisanOrderRepository
                              .findArtisanOrdersByOrder_Id(order.getId());

                    // Enrich artisan info (optional if already mapped by JPA)
                    for (ArtisanOrder ao : artisanOrders) {
                         if (ao.getArtisan() != null && ao.getArtisan().getId() != null) {
                              Artisan artisan = artisanRepository.findArtisanById(ao.getArtisan().getId());
                              if (artisan != null) {
                                   ao.setArtisan(artisan);
                              }
                         }
                    }

                    // 3. Map to DTO
                    OrderHistoryDTO dto = new OrderHistoryDTO();
                    dto.setOrderId(order.getId());
                    // dto.setOrderDate(order.getCreatedAt());
                    dto.setTotalAmount(order.getTotalAmount());
                    dto.setStatus(order.getOrderStatus());

                    // 4. Add to final list
                    response.add(dto);
               }

               return response;

          } catch (OrderException e) {
               throw e;
          } catch (Exception e) {
               throw new OrderException("Failed to fetch orders: " + e.getMessage(),
                         OrderErrorType.ORDER_CREATION_FAILED);
          }
     }

     @Override
     public List<OrderHistoryDTO> getOrdersByArtisan(String jwt) {

          try {
               UserResponseDTO userDtlsDto = userService.UserByToken(jwt);
               User user = abstractMapperService.toEntity(userDtlsDto, User.class);

               if (user == null) {
                    throw new OrderException("User not found", OrderErrorType.ORDER_NOT_FOUND);
               }

               // 1. Fetch all orders of this artisan
               List<Order> orders = orderRepository.findByArtisanOrders_Id(user.getUserId());

               List<OrderHistoryDTO> response = new ArrayList<>();

               // 2. For each order, build DTO
               for (Order order : orders) {

                    // Fetch artisan orders for this order
                    List<ArtisanOrder> artisanOrders = artisanOrderRepository
                              .findArtisanOrdersByOrder_Id(order.getId());

                    // Enrich artisan info (optional if already mapped by JPA)
                    for (ArtisanOrder ao : artisanOrders) {
                         if (ao.getArtisan() != null && ao.getArtisan().getId() != null) {
                              Artisan artisan = artisanRepository.findArtisanById(ao.getArtisan().getId());
                              if (artisan != null) {
                                   ao.setArtisan(artisan);
                              }
                         }
                    }

                    // 3. Map to DTO
                    OrderHistoryDTO dto = new OrderHistoryDTO();
                    dto.setOrderId(order.getId());
                    // dto.setOrderDate(order.getCreatedAt());
                    dto.setTotalAmount(order.getTotalAmount());
                    dto.setStatus(order.getOrderStatus());

                    // 4. Add to final list
                    response.add(dto);
               }

               return response;

          } catch (OrderException e) {
               throw e;
          } catch (Exception e) {
               throw new OrderException("Failed to fetch orders: " + e.getMessage(),
                         OrderErrorType.ORDER_CREATION_FAILED);
          }

     }

     @Override
     public String updatOrderStatus(String jwt, Integer artisanOrderId, String st) {
          try {
               ArtisanOrder artisanOrder = artisanOrderRepository
                         .findById(Long.valueOf(artisanOrderId))
                         .orElseThrow(
                                   () -> new OrderException("Artisan Order not found", OrderErrorType.ORDER_NOT_FOUND));

               if (artisanOrder.getPaymentStatus() != null &&
                         artisanOrder.getPaymentStatus().equals(com.example.demo.constants.PaymentStatus.FAILED)) {
                    throw new OrderException("Cannot update status for failed payment orders",
                              OrderErrorType.ORDER_UPDATE_FAILED);
               }
               artisanOrder.setStatus(ArtisanOrderStatus.valueOf(st));
               artisanOrderRepository.save(artisanOrder);

               return "Order status updated successfully";
          } catch (IllegalArgumentException e) {
               throw new OrderException("Invalid order status: " + st, OrderErrorType.INVALID_ORDER_STATUS);
          } catch (OrderException e) {
               throw e;
          } catch (Exception e) {
               throw new OrderException("Failed to update order status: " + e.getMessage(),
                         OrderErrorType.ORDER_UPDATE_FAILED);
          }
     }

     @Override
     public String cancelOrder(String orderId) {
          Optional<Order> order = orderRepository.findById(Long.valueOf(orderId));

          if (order.isPresent()) {
               order.get().setOrderStatus(OrderStatus.CANCELLED);
               orderRepository.save(order.get());
               return "Order cancelled successfully";
          }

          throw new OrderException("Order not found", OrderErrorType.ORDER_NOT_FOUND);
     }

     @Override
     public String paymentCallback() {
          // TODO Auto-generated method stub
          return null;
     }

     @Override
     public Long getOrderCount() {
          List<Order> orders = orderRepository.findAll();
          Long totalRevenue = 0L;
          for (Order o : orders) {
               totalRevenue += o.getTotalAmount().longValue();
          }
          return totalRevenue;
     }

}
