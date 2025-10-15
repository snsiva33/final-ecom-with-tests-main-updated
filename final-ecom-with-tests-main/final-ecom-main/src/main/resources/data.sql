INSERT INTO users (id, name, email, password, role) VALUES (1, 'Admin', 'admin@example.com', '$2a$10$e0Rq6XzZsXzKXjQk3JqIu.1u9uDk9sJQ3yN91V6v0N1gq6YkD0DqS', 'ROLE_ADMIN');
-- The above password is bcrypt('admin123') for convenience.

INSERT INTO products (id, name, description, price, stock, category) VALUES
(1, 'Phone Model A', 'A smartphone', 199.99, 50, 'Electronics'),
(2, 'T-Shirt Blue', 'Comfortable cotton t-shirt', 15.0, 200, 'Clothing'),
(3, 'Laptop B', 'Powerful laptop', 899.99, 10, 'Electronics');
