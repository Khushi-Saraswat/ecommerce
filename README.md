# 🧵 Artisan Marketplace Backend

A **multi-vendor e-commerce backend system** built using **Spring Boot, JPA, and PostgreSQL** where multiple artisans (sellers) can sell products and customers can purchase products from different sellers in a single order.

---

# ✨ Features

- Multi-vendor product selling  
- Order splitting per artisan  
- Product reviews and wishlist  
- Secure authentication with JWT  
- Payment processing  
- Cart and checkout system  

---

# 🏗 System Architecture Overview

The application follows a **multi-vendor marketplace architecture** where a user can act as both a **customer and an artisan (seller)**.

## Core Modules

- User Management  
- Artisan Management  
- Product Management  
- Cart System  
- Order Processing  
- Payment Handling  
- Review and Wishlist System  

---

# 👤 Core Actor

The **User** is the central entity of the system.

A user can:

- Browse products  
- Add products to cart  
- Place orders  
- Review products  
- Maintain wishlist  
- Become an artisan (seller)  

---

# 🔁 System Flow

The overall user journey in the system:
User Registration
│
▼
Browse Products
│
▼
Add Product to Cart
│
▼
Checkout
│
▼
Create Order
│
▼
Split Order by Artisan
│
▼
Create Order Items
│
▼
Process Payment
│
▼
Order Fulfillment

---

# 🧩 Entity Relationship Flow

The system is designed around **relational entities connected through JPA relationships**.

---

# 1️⃣ User Relationships

A **User** can perform multiple actions in the system.


| Entity | Description |
|------|------|
| Address | Stores multiple delivery addresses for the user |
| Cart | Temporary storage before checkout |
| Wishlist | Stores saved products |
| Order | Stores user purchases |
| Review | Product feedback |
| Payment | Payment details for orders |
| RefreshToken | Used for JWT authentication |

---

# 2️⃣ Artisan (Seller) Flow

A user can become an **Artisan (Seller)** to sell products.


| Entity | Description |
|------|------|
| Artisan | Seller profile |
| Product | Items sold by artisan |
| ProductImage | Images of the product |
| PriceHistory | Tracks price changes |
| CategoryRequest | Artisan requests new category |

---

# 3️⃣ Product Relationships

Products are the **core marketplace entity**.


| Entity | Description |
|------|------|
| ProductImage | Stores product images |
| PriceHistory | Tracks price changes |
| Review | User reviews |
| OrderItem | Product inside an order |
| Cart | Product added to cart |
| Wishlist | Saved product |

---

# 🛒 Cart Flow

The **Cart** stores products temporarily before checkout.

## Example Cart Entry

| user_id | product_id | quantity |
|-------|-------|--------|
| 10 | 23 | 2 |

Cart items are converted into an **Order during checkout**.

---

# 📦 Order Processing Flow

The system supports **multi-vendor orders**.

## Example

A user buys:

Product A → Artisan 1
Product B → Artisan 2

The system creates:
Order
├── ArtisanOrder (Artisan 1)
│ └── OrderItem
│
└── ArtisanOrder (Artisan 2)
└── OrderItem


### Why this design?

This allows:

- Independent seller order management  
- Separate shipping  
- Separate seller tracking  

---

# 💳 Payment Flow

Payments are linked to orders.
User
│
▼
Order
│
▼
Payment

### Payment stores

- Payment method  
- Transaction ID  
- Gateway response  
- Payment status  

---

# 📍 Address Flow

Users can store **multiple delivery addresses**.
User
│
▼
Address
│
▼
Order

Orders use **one selected address for delivery**.
