package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.Common.AbstractMapperService;
import com.example.demo.constants.ArtisanOrderStatus;
import com.example.demo.constants.OrderStatus;
import com.example.demo.constants.Role;
import com.example.demo.constants.errorTypes.AuthErrorType;
import com.example.demo.constants.errorTypes.CartErrorType;
import com.example.demo.constants.errorTypes.OrderErrorType;
import com.example.demo.constants.errorTypes.ProductErrorType;
import com.example.demo.constants.errorTypes.UserErrorType;
import com.example.demo.exception.Auth.AuthException;
import com.example.demo.exception.Cart.CartException;
import com.example.demo.exception.Order.OrderException;
import com.example.demo.exception.Product.ProductException;
import com.example.demo.exception.User.UserException;
import com.example.demo.model.Address;
import com.example.demo.model.Artisan;
import com.example.demo.model.ArtisanOrder;
import com.example.demo.model.Cart;
import com.example.demo.model.Order;
import com.example.demo.model.OrderItem;
import com.example.demo.model.Product;
import com.example.demo.model.User;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.ArtisanOrderRepository;
import com.example.demo.repository.ArtisanRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.Order.OrderRequestDTO;
import com.example.demo.response.Order.OrderHistoryDTO;
import com.example.demo.response.Order.OrderResponseDTO;
import com.example.demo.service.methods.AuthService;
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
     private ProductRepository productRepository;

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

     @Autowired
     private AuthService authService;

     @Autowired
     private OrderItemRepository orderItemrepo;

     // place orders

     @Override
     public String saveOrder(OrderRequestDTO orderRequestDTO) {
          try {

               User user = authService.getCurrentUser();
               if (user == null) {
                    throw new AuthException("jwt token is invalid !!", AuthErrorType.TOKEN_INVALID);
               }

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

               Address address = addressRepository.findById(Integer.valueOf(orderRequestDTO.getAddressId()))
                         .orElseThrow(() -> new OrderException("Address not found", OrderErrorType.ADDRESS_NOT_FOUND));

               // Step 2: Create Parent Order
               Order order = new Order();
               order.setUser(user);
               order.setAddress(address);
               order.setPaymentType(orderRequestDTO.getPaymentType());
               order.setOrderStatus(OrderStatus.PLACED);
               order.setTotalAmount(BigDecimal.valueOf(sum));

               // Step 3: Group cart by Artisan
               // Ye stream har cart item ko uthata hai aur uske product ke artisan
               // ke according map me group kar deta hai.
               /*
                * 
                * Cart me items:
                * 
                * Item Artisan
                * Ring A1
                * Necklace A2
                * Bracelet A1
                * 
                * After grouping:
                * 
                * A1 → [Ring, Bracelet]
                * A2 → [Necklace]
                * 
                */
               Map<Artisan, List<Cart>> map = cart.stream().collect(Collectors.groupingBy(
                         c -> c.getProduct().getArtisan()));

               // simulate payment here for full order
               // add fake razorpay payment...

               if (orderRequestDTO.getPaymentType().equalsIgnoreCase("online")) {
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
               // Step 4: Create Artisan Orders for each artisan
               List<ArtisanOrder> artisanOrders = new ArrayList<>();

               for (Map.Entry<Artisan, List<Cart>> entry : map.entrySet()) {
                    Artisan artisan = entry.getKey();
                    List<Cart> artisanCarts = entry.getValue();

                    Artisan artisans = artisanRepository.findById(artisan.getId())
                              .orElseThrow(() -> new OrderException("Artisan not found: " + artisan.getId(),
                                        OrderErrorType.ORDER_CREATION_FAILED));

                    ArtisanOrder artisanOrder = new ArtisanOrder();
                    artisanOrder.setOrder(order);
                    artisanOrder.setArtisan(artisan);
                    artisanOrder.setRazorpayOrderId(order.getRazorpayOrderId());
                    artisanOrder.setPaymentStatus(order.getPaymentStatus());

                    double subtotal = 0;
                    List<OrderItem> orderItems = new ArrayList<>();
                    for (Cart c : artisanCarts) {

                         Product product = c.getProduct();

                         if (product.getStock() != null && c.getQuantity() > product.getStock()) {
                              throw new ProductException("Stock not available for product: " + product.getName(),
                                        ProductErrorType.OUT_OF_STOCK);
                         }

                         OrderItem item = new OrderItem();
                         item.setArtisanOrder(artisanOrder);
                         item.setProduct(product);
                         item.setQuantity(c.getQuantity());

                         subtotal += c.getProduct().getPrice() * c.getQuantity();
                         item.setItemTotal(subtotal);

                         orderItems.add(item);

                         // reduce stock
                         product.setStock(product.getStock() - c.getQuantity());

                    }
                    artisanOrder.setSubtotal(BigDecimal.valueOf(subtotal));
                    artisanOrder.setOrder(order);
                    artisanOrder.setItems(orderItems);
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
     public Page<OrderHistoryDTO> getOrdersByUser(Pageable pageable) {

          try {
               User user = authService.getCurrentUser();

               if (user == null) {

                    throw new AuthException("jwt token is invalid !!", AuthErrorType.TOKEN_INVALID);

               }

               // 1. Fetch all orders of this user
               Page<Order> orders = orderRepository.findByUser_UserId(user.getUserId(), pageable);

               return orders.map(order -> {
                    OrderHistoryDTO dto = new OrderHistoryDTO();
                    dto.setOrderId(order.getId());
                    dto.setTotalAmount(order.getTotalAmount());
                    dto.setStatus(order.getOrderStatus());
                    return dto;
               });

          } catch (OrderException e) {
               throw e;
          } catch (Exception e) {
               throw new OrderException("Failed to fetch orders: " + e.getMessage(),
                         OrderErrorType.ORDER_CREATION_FAILED);
          }
     }

     @Override
     public Page<OrderHistoryDTO> getOrdersByArtisan(Pageable pageable) {

          try {

               User user = authService.getCurrentUser();
               if (user == null) {
                    throw new AuthException("jwt token is invalid !!", AuthErrorType.TOKEN_INVALID);
               }

               Page<ArtisanOrder> artisanOrderPage = artisanOrderRepository.findByArtisan_Id(user.getUserId(),
                         pageable);

               return artisanOrderPage.map(
                         artisanOrder -> {
                              Order order = artisanOrder.getOrder();

                              OrderHistoryDTO dto = new OrderHistoryDTO();
                              dto.setOrderId(order.getId());
                              dto.setTotalAmount(order.getTotalAmount());
                              dto.setStatus(order.getOrderStatus());

                              return dto;
                         });

          } catch (OrderException e) {
               throw e;
          } catch (Exception e) {
               throw new OrderException("Failed to fetch orders: " + e.getMessage(),
                         OrderErrorType.ORDER_CREATION_FAILED);
          }

     }

     @Override
     public String updatOrderStatus(Long artisanOrderId, String st) {
          try {

               User user = authService.getCurrentUser();
               if (user == null) {
                    throw new AuthException("jwt token is invalid !!", AuthErrorType.TOKEN_INVALID);
               }
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
     public String cancelOrder(Long orderId) throws BadRequestException {

          User user = authService.getCurrentUser();
          if (user == null) {
               throw new AuthException("jwt token is invalid !!", AuthErrorType.TOKEN_INVALID);
          }

          Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new OrderException("Order is not found", OrderErrorType.ORDER_NOT_FOUND));

          if (!order.getUser().getUserId().equals(user.getUserId())) {
               throw new BadRequestException("Access denied");
          }

          // Cancel allowed only before shipped/delivered
          if (order.getOrderStatus() == OrderStatus.SHIPPED ||
                    order.getOrderStatus() == OrderStatus.DELIVERED) {
               throw new BadRequestException("Order cannot be cancelled at this stage");
          }

          // Restore stock for each product in all artisan orders
          List<ArtisanOrder> artisanOrders = artisanOrderRepository.findArtisanOrdersByOrder_Id(orderId);

          for (ArtisanOrder a : artisanOrders) {
               List<OrderItem> items = orderItemrepo.findByArtisanOrder_Order_Id(a.getId());
               for (OrderItem item : items) {
                    Product product = item.getProduct();
                    product.setStock(product.getStock() + item.getQuantity());
                    productRepository.save(product);
               }
          }

          // Cancel the order
          order.setOrderStatus(OrderStatus.CANCELLED);
          orderRepository.save(order);

          return "Order cancelled successfully";
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

     @Override
     public OrderResponseDTO getOrderById(Long orderId) {
          Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new OrderException("Order is not found", OrderErrorType.ORDER_NOT_FOUND));

          return abstractMapperService.toDto(order, OrderResponseDTO.class);
     }

     @Override
     public String updateAllOrderStatus(Long orderId, String status) {

          User user = authService.getCurrentUser();
          if (user == null) {
               throw new AuthException("jwt token is invalid !!", AuthErrorType.TOKEN_INVALID);
          }

          Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new OrderException("Order is not found", OrderErrorType.ORDER_NOT_FOUND));

          if (order.getPaymentStatus() != null &&
                    order.getPaymentStatus().equals(com.example.demo.constants.PaymentStatus.FAILED)) {
               throw new OrderException("Cannot update status for failed payment orders",
                         OrderErrorType.ORDER_UPDATE_FAILED);
          }

          order.setOrderStatus(OrderStatus.valueOf(status));

          order = orderRepository.save(order);

          if (order != null)
               return "order is updated";

          return "order is not updated";
     }

     @Override
     public OrderResponseDTO updateOrderStatusByArtisan(Long orderId, OrderStatus status) {

          User user = authService.getCurrentUser();

          if (user == null || user.getRole() != Role.ARTISAN) {
               throw new UserException("Only artisan can update order",
                         UserErrorType.UNAUTHORIZED);
          }

          Artisan artisan = artisanRepository
                    .findByUser_UserId(user.getUserId())
                    .orElseThrow(() -> new UserException(
                              "Artisan not found",
                              UserErrorType.NOT_FOUND));

          Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new OrderException(
                              "Order not found",
                              OrderErrorType.ORDER_NOT_FOUND));

          // Prevent invalid transitions
          if (order.getOrderStatus() == OrderStatus.DELIVERED) {
               throw new OrderException("Delivered order cannot be modified",
                         OrderErrorType.INVALID_ORDER_STATUS);
          }

          order.setOrderStatus(status);

          Order saved = orderRepository.save(order);

          return abstractMapperService.toDto(saved, OrderResponseDTO.class);

     }

}
