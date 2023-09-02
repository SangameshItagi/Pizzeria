/* SQL Script Developed By Sangamesh Itagi and Gourish Pisal*/

DROP SCHEMA IF EXISTS Pizzeria;
CREATE DATABASE Pizzeria;
USE Pizzeria;

CREATE TABLE `basepizza` (
  `PizzaSize` varchar(15) NOT NULL,
  `PizzaCrust` varchar(15) NOT NULL,
  `PizzaPrice` decimal(4,2) NOT NULL,
  `PizzaCost` decimal(4,2) NOT NULL,
  PRIMARY KEY (`PizzaSize`,`PizzaCrust`)
);

CREATE TABLE `customer` (
  `CustomerId` int NOT NULL AUTO_INCREMENT,
  `CustomerName` varchar(50) NOT NULL,
  `CustomerPhone` varchar(25) DEFAULT NULL,
  PRIMARY KEY (`CustomerId`)
) ;

CREATE TABLE `topping` (
  `ToppingId` int NOT NULL auto_increment,
  `ToppingName` varchar(20) NOT NULL,
  `ToppingPrice` decimal(4,2) NOT NULL,
  `ToppingCost` decimal(4,2) NOT NULL,
  `ToppingPersonalUnits` decimal(4,2) NOT NULL,
  `ToppingMediumUnits` decimal(4,2) NOT NULL,
  `ToppingLargeUnits` decimal(4,2) NOT NULL,
  `ToppingXlargeUnits` decimal(4,2) NOT NULL,
  `ToppingInventory` decimal(7,2) NOT NULL,
  PRIMARY KEY (`ToppingId`)
) ;
CREATE TABLE `discount` (
  `DiscountId` int NOT NULL AUTO_INCREMENT,
  `DiscountName` varchar(20) NOT NULL,
  `IsPercent` BOOLEAN DEFAULT false,
  `DiscountAmount` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`DiscountId`)
) ;
CREATE TABLE `order` (
  `OrderId` int NOT NULL AUTO_INCREMENT,
  `OrderCost` decimal(8,2) NOT NULL,
  `OrderType` varchar(10) NOT NULL,
  `OrderTime` datetime NOT NULL,
  `OrderPrice` decimal(8,2) NOT NULL,
  `CustomerId` int DEFAULT NULL,
  `IsComplete` BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (`OrderId`),
  KEY `CustomerId` (`CustomerId`),
  CONSTRAINT `order_fk_1` FOREIGN KEY (`CustomerId`) REFERENCES `customer` (`CustomerId`),
  CONSTRAINT `order_chk_1` CHECK (((`ordertype` = _utf8mb4'dinein') or (`ordertype` = _utf8mb4'delivery') or (`ordertype` = _utf8mb4'pickup')))
);

CREATE TABLE `dinein` (
  `OrderId` int NOT NULL,
  `TableNumber` decimal(5,0) NOT NULL,
  PRIMARY KEY (`OrderId`),
  CONSTRAINT `dinein_fk_1` FOREIGN KEY (`OrderId`) REFERENCES `order` (`OrderId`)
) ;

CREATE TABLE `delivery` (
  `OrderId` int NOT NULL,
  `CustomerAddress` varchar(50) NOT NULL,
  PRIMARY KEY (`OrderId`),
  CONSTRAINT `delivery_fk_1` FOREIGN KEY (`OrderId`) REFERENCES `order` (`OrderId`)
) ;
CREATE TABLE `pickup` (
  `OrderId` int NOT NULL,
  PRIMARY KEY (`OrderId`),
  CONSTRAINT `pickup_fk_1` FOREIGN KEY (`OrderId`) REFERENCES `order` (`OrderId`)
) ;
CREATE TABLE `pizza` (
  `PizzaId` int NOT NULL AUTO_INCREMENT,
  `OrderId` int NOT NULL,
  `PizzaSize` varchar(15) NOT NULL,
  `PizzaCrust` varchar(15) NOT NULL,
  `PizzaState` varchar(15) DEFAULT 'PROCESSED',
  `PizzaCost` decimal(8,2) NOT NULL,
  `PizzaPrice` decimal(8,2) NOT NULL,
  PRIMARY KEY (`PizzaId`),
  KEY `OrderId` (`OrderId`),
  KEY `PizzaSize` (`PizzaSize`,`PizzaCrust`),
  CONSTRAINT `pizza_fk_1` FOREIGN KEY (`OrderId`) REFERENCES `order` (`OrderId`),
  CONSTRAINT `pizza_fk_2` FOREIGN KEY (`PizzaSize`, `PizzaCrust`) REFERENCES `basepizza` (`PizzaSize`, `PizzaCrust`)
) ;

CREATE TABLE `pizzatopping` (
  `PizzaId` int NOT NULL,
  `ToppingId` int NOT NULL,
  `ExtraTopping` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`PizzaId`,`ToppingId`),
  KEY `ToppingId` (`ToppingId`),
  CONSTRAINT `pizzatopping_fk_1` FOREIGN KEY (`PizzaId`) REFERENCES `pizza` (`PizzaId`),
  CONSTRAINT `pizzatopping_fk_2` FOREIGN KEY (`ToppingId`) REFERENCES `topping` (`ToppingId`)
);

CREATE TABLE `orderdiscount` (
  `OrderId` int NOT NULL,
  `DiscountId` int NOT NULL,
  PRIMARY KEY (`OrderId`,`DiscountId`),
  KEY `DiscountId` (`DiscountId`),
  CONSTRAINT `orderdiscount_fk_1` FOREIGN KEY (`OrderId`) REFERENCES `order` (`OrderId`),
  CONSTRAINT `orderdiscount_fk_2` FOREIGN KEY (`DiscountId`) REFERENCES `discount` (`DiscountId`)
);

CREATE TABLE `pizzadiscount` (
  `PizzaId` int NOT NULL,
  `DiscountId` int NOT NULL,
  PRIMARY KEY (`PizzaId`,`DiscountId`),
  KEY `DiscountId` (`DiscountId`),
  FOREIGN KEY (`PizzaId`) REFERENCES `pizza` (`PizzaId`),
  FOREIGN KEY (`DiscountId`) REFERENCES `discount` (`DiscountId`)
);