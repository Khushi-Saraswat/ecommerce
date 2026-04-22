# 🛍️ Multi-Vendor Artisan Marketplace — Backend

A Spring Boot based backend for a **multi-vendor artisan marketplace** where artisans can sell products and users can purchase items from different sellers in a single order.

This project focuses on **clean entity design**, **order splitting**, **product catalog**, and **secure authentication**.

---

# 🧩 Core Entities

## 👤 User

Represents application users (ADMIN / USER / ARTISAN)

**Fields**

* userId
* name
* username (email)
* password
* mobileNumber
* role
* city
* state
* createdAt
* isBlocked

**Relations**

* User → Orders
* User → Cart
* User → Reviews
* User → Address

---

## 🧑‍🎨 Artisan

Represents sellers in marketplace.

**Fields**

* id
* brandName
* artisianType
* bio
* city
* state
* pincode
* kycStatus
* createdAt

**Relations**

* Artisan → Products
* Artisan → ArtisanOrders
* Artisan → CategoryRequest

---

## 📦 Product

Represents items sold by artisans.

**Fields**

* id
* name
* slug
* description
* price
* mrp
* stock
* featured_product
* isActive
* createdAt

**Relations**

* Product → Artisan
* Product → Category
* Product → ProductImage
* Product → Review
* Product → Cart

---

## 🖼 ProductImage

Stores product images uploaded to Cloudinary.

**Fields**

* id
* imageUrl
* imageName
* imageType

**Relations**

* ProductImage → Product

---

## 🗂 Category

Product category.

**Fields**

* id
* name
* description
* slug
* imageUrl
* isActive
* createdAt

**Relations**

* Category → Products

---

## 🛒 Cart

Stores items before checkout.

**Fields**

* id
* quantity
* totalPrice (Transient)

**Relations**

* Cart → User
* Cart → Product
* Cart → Artisan

---

## 📦 Order

Represents main order placed by user.

**Fields**

* id
* totalAmount
* orderStatus
* paymentStatus
* paymentType
* razorpayOrderId

**Relations**

* Order → User
* Order → Address
* Order → ArtisanOrder

---

## 🏪 ArtisanOrder

Splits order per artisan.

**Fields**

* id
* subtotal
* status
* paymentStatus
* createdAt

**Relations**

* ArtisanOrder → Order
* ArtisanOrder → Artisan
* ArtisanOrder → OrderItem

---

## 📄 OrderItem

Products inside artisan order.

**Fields**

* id
* quantity
* price
* itemTotal

**Relations**

* OrderItem → ArtisanOrder
* OrderItem → Product

---

## 💳 Payment

Tracks payment details.

**Fields**

* id
* amount
* currency
* method
* status
* transactionId
* gatewayResponse

**Relations**

* Payment → Order
* Payment → User

---

## ⭐ Review

User product rating.

**Fields**

* id
* rating
* comment
* createdAt

**Relations**

* Review → Product
* Review → User

---

## 📍 Address

Shipping address.

**Fields**

* id
* addressName
* addressLandMark
* addressState
* addressPhoneNumber
* addressZipCode
* city
* defaultAddress

**Relations**

* Address → User
* Address → Order

---

## 📝 Feedback

User feedback for product.

**Fields**

* id
* fullName
* email
* category
* message
* date

**Relations**

* Feedback → User
* Feedback → Product

---

## 📊 CategoryRequest

Artisan request for new category.

**Fields**

* id
* name
* status
* createdAt

**Relations**

* CategoryRequest → Artisan

---

## 📈 PriceHistory

Tracks product price changes.

**Fields**

* id
* oldPrice
* newPrice
* changedAt
* changedBy

**Relations**

* PriceHistory → Product

---

# 🔄 Order Flow

User adds products to cart
→ checkout
→ Order created
→ split into ArtisanOrder
→ each artisan handles own order

---

# ⚙️ Key Features

* Multi vendor marketplace
* Product catalog
* Order splitting per artisan
* Cart management
* Payment tracking
* Review system
* Category management
* Address mapping
* Feedback system
* Cloudinary image upload
* Pagination support
* Product indexing
* Caching (product & category)

---

# 🛠 Tech Stack

* Java
* Spring Boot
* Spring Security
* JWT
* Spring Data JPA
* Hibernate
* PostgreSQL / MySQL
* Cloudinary
* Lombok

---

# 📁 Architecture

Controller
Service
Repository
Entity
DTO
Security
Config

---

# 👩‍💻 Author

Khushi Saraswat
Spring Boot Backend Developer
