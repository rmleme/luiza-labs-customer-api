-- MySQL

CREATE TABLE customer (
  customer_id BIGINT AUTO_INCREMENT,
  id BINARY(16) NOT NULL,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(100) NOT NULL,
  PRIMARY KEY (customer_id),
  CONSTRAINT id_unique UNIQUE (id),
  CONSTRAINT email_unique UNIQUE (email)
);

CREATE TABLE product (
  product_id BIGINT AUTO_INCREMENT,
  id BINARY(16) NOT NULL,
  customer_id BIGINT NOT NULL,
  price DOUBLE NOT NULL,
  image VARCHAR(255) NOT NULL,
  brand VARCHAR(50) NOT NULL,
  title VARCHAR(100) NULL,
  review_score DOUBLE NULL,
  PRIMARY KEY (product_id),
  CONSTRAINT customer_fk FOREIGN KEY (customer_id) REFERENCES customer (customer_id) ON DELETE CASCADE,
  CONSTRAINT product_customer_unique UNIQUE (id, customer_id)
);