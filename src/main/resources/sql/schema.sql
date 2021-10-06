DROP TABLE IF EXISTS CUSTOMER;

CREATE TABLE CUSTOMER (
    CUST_ID   INTEGER      NOT NULL AUTO_INCREMENT,
    FIRST_NAME VARCHAR(128) NOT NULL,
    LAST_NAME VARCHAR(128) NOT NULL,
    NICK_NAME VARCHAR(128) NOT NULL,
    PHONE VARCHAR(128) NOT NULL,
    ADDR VARCHAR(128) NOT NULL,
    PRIMARY KEY (CUST_ID)
);

DROP TABLE IF EXISTS PRODUCT;

CREATE TABLE PRODUCT (
    PROD_ID   INTEGER      NOT NULL AUTO_INCREMENT,
    NAME VARCHAR(128) NOT NULL,
    DESC TEXT NOT NULL,
    PRICE DOUBLE NOT NULL,
    PRIMARY KEY (PROD_ID)
);

DROP TABLE IF EXISTS SHOPPINGORDER;
CREATE TABLE SHOPPINGORDER (
    ORDER_ID   INTEGER      NOT NULL AUTO_INCREMENT,
    RECIPIENT VARCHAR(128) NOT NULL,
    SHIPPING_ADDR VARCHAR(128) NOT NULL,
    ORDER_DATETIME TIMESTAMP NOT NULL,
    LASTMODIFIED TIMESTAMP  NOT NULL,
    CUST_ID VARCHAR(128) NULL, 
    AMOUNT DOUBLE NULL,
    DISCOUNT DOUBLE NULL,
    PRIMARY KEY (ORDER_ID)
);

DROP TABLE IF EXISTS SHOPPINGORDERITEM;
CREATE TABLE SHOPPINGORDERITEM (
  ORDER_ITEM_ID INTEGER NOT NULL AUTO_INCREMENT,
  ORDER_ID INTEGER NOT NULL,
  PROD_ID INTEGER NOT NULL,
  QUANTITY INTEGER NOT NULL,
  ORDER_PRICE DOUBLE NULL,
  PRIMARY KEY (ORDER_ITEM_ID),
  FOREIGN KEY(ORDER_ID) REFERENCES SHOPPINGORDER(ORDER_ID),
  FOREIGN KEY(PROD_ID) REFERENCES PRODUCT(PROD_ID)
);