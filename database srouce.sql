CREATE DATABASE ecommerce;
use ecommerce;

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    price DECIMAL(12,2) NOT NULL,
    stock INT NOT NULL,
    category_id BIGINT NOT NULL,

    CONSTRAINT fk_product_category
        FOREIGN KEY (category_id)
        REFERENCES categories(id)
);

CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    total_amount DECIMAL(12,2) NOT NULL,
    status VARCHAR(30) NOT NULL,

    CONSTRAINT fk_order_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);

CREATE TABLE order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(12,2) NOT NULL,

    CONSTRAINT fk_order_item_order
        FOREIGN KEY (order_id)
        REFERENCES orders(id),

    CONSTRAINT fk_order_item_product
        FOREIGN KEY (product_id)
        REFERENCES products(id)
);

CREATE TABLE cart (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL UNIQUE,

    CONSTRAINT fk_cart_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
);

CREATE TABLE cart_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    cart_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL,

    CONSTRAINT fk_cart_item_cart
        FOREIGN KEY (cart_id)
        REFERENCES cart(id),

    CONSTRAINT fk_cart_item_product
        FOREIGN KEY (product_id)
        REFERENCES products(id)
);


ALTER TABLE cart_items AUTO_INCREMENT = 1;
ALTER TABLE order_items AUTO_INCREMENT = 1;
ALTER TABLE cart AUTO_INCREMENT = 1;
ALTER TABLE orders AUTO_INCREMENT = 1;
ALTER TABLE products AUTO_INCREMENT = 1;
ALTER TABLE categories AUTO_INCREMENT = 1;
ALTER TABLE users AUTO_INCREMENT = 1;

INSERT INTO categories (name)
VALUES
('Watch'),
('Laptop'),
('Tablet'),
('Phone');


INSERT INTO products (name, price, stock, category_id)
VALUES
('Samsung Galaxy Watch 6', 300.00, 20, 1),
('Xiaomi Watch 2',          250.00, 20, 1),
('iPhone Watch Series 1',   500.00, 20, 1),
('Dell Smart Watch',        350.00, 20, 1),
('Samsung Galaxy Watch 7',  700.00, 20, 1);

INSERT INTO products (name, price, stock, category_id)
VALUES
('Dell Inspiron 15',        800.00, 20, 2),
('Samsung Galaxy Book 4',  1200.00, 20, 2),
('Xiaomi RedmiBook 15',     900.00, 20, 2),
('Dell XPS 13',             1500.00, 20, 2),
('Samsung Galaxy Book 5',   1800.00, 20, 2);

INSERT INTO products (name, price, stock, category_id)
VALUES
('Samsung Galaxy Tab S9',   700.00, 20, 3),
('Xiaomi Pad 6',            500.00, 20, 3),
('iPhone Tablet Pro',       1200.00, 20, 3),
('Dell Latitude Tablet',    900.00, 20, 3),
('Samsung Galaxy Tab S10', 1500.00, 20, 3);

INSERT INTO products (name, price, stock, category_id)
VALUES
('Samsung Galaxy S24',      900.00, 20, 4),
('Xiaomi 14',               700.00, 20, 4),
('iPhone 15',              1000.00, 20, 4),
('Dell Mobile Pro',         600.00, 20, 4),
('Samsung Galaxy S25',     1400.00, 20, 4);


