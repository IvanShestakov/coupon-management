CREATE DATABASE coupons;
USE coupons;


GRANT ALL ON coupons.* to 'Admin'@'localhost' IDENTIFIED BY '1234';

CREATE TABLE company (
id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
comp_name VARCHAR(50),
password VARCHAR(12),
email VARCHAR(255)
);

CREATE TABLE customer (
id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
cust_name VARCHAR(255),
password VARCHAR(12)
);

CREATE TABLE coupon (
id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
title VARCHAR(120),
start_date DATE,
end_date DATE,
amount INT,
coupon_type VARCHAR(50),
message VARCHAR(1024),
price DOUBLE,
image VARCHAR(255)
);

CREATE TABLE customer_coupon (
cust_id INT,
coupon_id INT
);

CREATE TABLE company_coupon (
comp_id INT NOT NULL,
coupon_id INT NOT NULL,
CONSTRAINT pk_comp_coupon PRIMARY KEY (comp_id, coupon_id)

);

