🌟 CraftConnect – Local Artisan Marketplace Backend

Spring Boot | MySQL | Elasticsearch | JWT Security

Trendify is a complete e-commerce backend system designed with clean architecture, secure authentication, advanced search, filtering, sorting, and admin management features.

🏗 System Architecture & Entity Relationships

👤 User

Represents all platform users (Customer, Artisan, Admin).

Responsibilities:

Authentication & JWT security

Role-based authorization

Placing orders, writing reviews, managing cart

Relationships:

One User → One Artisan (if approved seller)

One User → Many Orders

One User → Many Reviews

One User → One Refresh Token

🧵 Artisan

Seller profile linked to a User.

Responsibilities:

KYC verification

Product management

Order fulfillment

Relationships:

One Artisan → One User (OneToOne)

One Artisan → Many Products

One Artisan → Many ArtisanOrders

📦 Product

Stores product catalog details.

Responsibilities:

Pricing, stock, category

Image gallery & attributes

Customer reviews

Relationships:

Many Products → One Artisan

One Product → Many Images

One Product → Many Attributes

One Product → Many Reviews

One Product → Many Cart Items

One Product → Many PriceHistory entries

JPA Config:

Cascade ALL for images & attributes

Orphan removal to delete unused records

🛒 Cart

Temporary shopping basket before checkout.

Relationships:

Many Cart Items → One User

Many Cart Items → One Product

Many Cart Items → One Artisan

🧾 Order

Customer purchase transaction.

Relationships:

Many Orders → One User

One Order → Many ArtisanOrders

🔀 ArtisanOrder (Order Split Layer)

Splits a single order into multiple seller-wise orders.

Purpose:

Each artisan tracks only their own items & delivery status.

Relationships:

Many ArtisanOrders → One Order

Many ArtisanOrders → One Artisan

⭐ Review

Product feedback by users.

Relationships:

Many Reviews → One Product

Many Reviews → One User

💬 Feedback

General platform feedback.

Relationships:

Many Feedback → One User

Many Feedback → One Product (optional)

💰 PriceHistory

Tracks product price changes.

Relationships:

Many PriceHistory → One Product

🔐 RefreshToken

Secure JWT token refresh handling.

Relationships:

One RefreshToken → One User






