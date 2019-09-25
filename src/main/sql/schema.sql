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



CREATE TABLE user (
  username VARCHAR(16) NOT NULL,
  password VARCHAR(16) NOT NULL,
  PRIMARY KEY (username)
);

CREATE TABLE user_role (
  username VARCHAR(16) NOT NULL,
  role VARCHAR(32) NOT NULL,
  PRIMARY KEY (username, role),
  CONSTRAINT user_fk FOREIGN KEY (username) REFERENCES user (username)
);



INSERT INTO user (username, password) values ('admin', '123');
INSERT INTO user_role (username, role) values ('admin', 'CUSTOMER_READ_ROLE');
INSERT INTO user_role (username, role) values ('admin', 'CUSTOMER_PERSIST_ROLE');

INSERT INTO user (username, password) values ('readonly', '123');
INSERT INTO user_role (username, role) values ('readonly', 'CUSTOMER_READ_ROLE');

COMMIT;