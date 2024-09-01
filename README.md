<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Ecommerce Project</title>
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
  <div class="container mt-5">
    <h1 class="mb-4">Ecommerce Project</h1>
    <p>Welcome to the Ecommerce Project! This project is a comprehensive online shopping platform with various functionalities for both administrators and customers. Below is a detailed description of the project, technologies used, and features included.</p>

    <h2>Technologies Used</h2>
    <h3>Frontend</h3>
    <ul>
      <li>HTML</li>
      <li>CSS</li>
      <li>Bootstrap</li>
      <li>Thymeleaf</li>
    </ul>
    <h3>Backend</h3>
    <ul>
      <li>Spring Boot 3.0</li>
      <li>Spring Security 6.0</li>
    </ul>
    <h3>Database</h3>
    <ul>
      <li>MySQL</li>
    </ul>
    <h3>Tools</h3>
    <ul>
      <li>VSCode</li>
    </ul>

    <h2>Features</h2>
    <h3>Admin Functionalities</h3>
    <ul>
      <li>Product Management: Add, edit, delete products.</li>
      <li>Category Management: Add, edit, delete categories.</li>
      <li>Order Management: View and manage customer orders, including delivery details.</li>
      <li>User Management: View logged-in user details, activate/deactivate accounts, and handle wrong login attempts.</li>
      <li>Admin Management: Add multiple admins, view existing admins, and activate/deactivate admin accounts.</li>
      <li>Feedback Dashboard: View and analyze customer feedback, categorized by total feedback, product feedback, customer feedback, review feedback, and others.</li>
    </ul>

    <h3>Customer Functionalities</h3>
    <ul>
      <li>User Registration & Login: Register and log in to the platform.</li>
      <li>Forgot Password: Reset password via email functionality.</li>
      <li>Account Locking: Account is locked after three consecutive wrong password attempts.</li>
      <li>Product Search: Search for products.</li>
      <li>Shopping Cart: Add products to the cart, view cart items, and proceed to checkout.</li>
      <li>Order Management: Place orders, view order history, cancel orders.</li>
      <li>Wishlist: Add products to a wishlist, manage wishlist items, and move items from wishlist to cart.</li>
      <li>Feedback Form: Provide feedback on products, services, reviews, etc.</li>
      <li>Profile Management: Update user profile and change password.</li>
      <li>Pagination: Navigate through paginated product lists and other sections.</li>
    </ul>

    <h2>Getting Started</h2>
    <h3>Prerequisites</h3>
    <ul>
      <li>Java 17 or higher for running Spring Boot</li>
      <li>MySQL Server for database management</li>
      <li>Maven for building the project</li>
    </ul>

    <h3>Installation</h3>
    <ol>
      <li>Clone the repository:
        <pre><code>git clone https://github.com/yourusername/ecommerce-project.git</code></pre>
      </li>
      <li>Navigate to the project directory:
        <pre><code>cd ecommerce-project</code></pre>
      </li>
      <li>Configure the database:
        <ul>
          <li>Update <code>application.properties</code> with your MySQL database configuration.</li>
        </ul>
      </li>
      <li>Build the project:
        <pre><code>mvn clean install</code></pre>
      </li>
      <li>Run the application:
        <pre><code>mvn spring-boot:run</code></pre>
      </li>
      <li>Access the application:
        <ul>
          <li>Open your browser and go to <a href="http://localhost:8080">http://localhost:8080</a>.</li>
        </ul>
      </li>
    </ol>

    <h2>Screenshots</h2>
    <div class="row">
      <div class="col-md-6">
        <h3>Admin Dashboard:</h3>
        <img src="assets/admin-dashboard.png" class="img-fluid" alt="Admin Dashboard">
      </div>
      <div class="col-md-6">
        <h3>Customer Dashboard:</h3>
        <img src="assets/customer-dashboard.png" class="img-fluid" alt="Customer Dashboard">
      </div>
    </div>

    <h2>Contributing</h2>
    <p>If you'd like to contribute to this project, please fork the repository and create a pull request with your changes. Make sure to follow the coding standards and write tests for your new features.</p>

    <h2>License</h2>
    <p>This project is licensed under the MIT License - see the <a href="LICENSE">LICENSE</a> file for details.</p>

    <h2>Contact</h2>
   
