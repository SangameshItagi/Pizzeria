/* SQL Script Developed By Sangamesh Itagi and Gourish Pisal*/
USE Pizzeria;
INSERT INTO topping (ToppingName,Toppingprice,ToppingCost,ToppingInventory,ToppingPersonalUnits,ToppingMediumUnits,ToppingLargeUnits,ToppingXlargeUnits)
VALUES
('Pepperoni',1.25,0.2,100,2,2.75,3.5,4.5),
('Sausage',1.25,0.15,100,2.5,3,3.5,4.25),
('Ham',1.5,0.15,78,2,2.5,3.25,4),
('Chicken',1.75,0.25,56,1.5,2,2.25,3),
('Green Pepper',0.5,0.02,79,1,1.5,2,2.5),
('Onion',0.5,0.02,85,1,1.5,2,2.75),
('Roma Tomato',0.75,0.03,86,2,3,3.5,4.5),
('Mushrooms',0.75,0.1,52,1.5,2,2.5,3),
('Black Olives',0.6,0.1,39,0.75,1,1.5,2),
('Pineapple',1,0.25,15,1,1.25,1.75,2),
('Jalapenos',0.5,0.05,64,0.5,0.75,1.25,1.75),
('Banana Peppers',0.5,0.05,36,0.6,1,1.3,1.75),
('Regular Cheese',1.5,0.12,250,2,3.5,5,7),
('Four Cheese Blend',2,0.15,150,2,3.5,5,7),
('Feta Cheese',2,0.18,75,1.75,3,4,5.5),
('Goat Cheese ',2,0.2,54,1.6,2.75,4,5.5),
('Bacon',1.5,0.25,89,1,1.5,2,3);

INSERT INTO discount (DiscountName,DiscountAmount,DiscountPercent)
VALUES
('Employee',NULL,15),
('Lunch Special Medium',1,NULL),
('Lunch Special Large',2,NULL),
('Speciality Pizza	',1.5,NULL),
('Gameday Special',NULL,20);

INSERT INTO basepizza(PizzaSize,PizzaCrust,PizzaPrice,PizzaCost)
VALUES
('small','Thin',3,0.5),
('small','Original',3,0.75),
('small','Pan',3.5,1),
('small','Gluten-Free',4,2),
('medium','Thin',5,1),
('medium','Original',5,1.5),
('medium','Pan',6,2.25),
('medium','Gluten-Free',6.25,3),
('large','Thin',8,1.25),
('large','Original',8,2),
('large','Pan',9,3),
('large','Gluten-Free',9.5,4),
('x-large','Thin',10,2),
('x-large','Original',10,3),
('x-large','Pan',11.25,4.5),
('x-large','Gluten-Free',12.25,6);

INSERT INTO customer(CustomerName,CustomerPhone)
VALUES
('Ellis Beck','864-254-5861'),
('Kurt McKinney','864-474-9953'),
('Calvin Sanders','864-232-8944'),
('Lance Benton','864-878-5679.');

INSERT INTO `order`(OrderCost,OrderType,OrderTime,OrderPrice,CustomerId)
VALUES
(3.68,'dinein','2022-03-05 12:03:00',13.5,NULL),
(4.63,'dinein','2022-03-03 12:05:00',17.35,NULL),
(19.8,'pickup','2022-03-03 21:30:00',64.5,1),
(16.86,'delivery','2022-03-05 19:11:00',45.5,1),
(7.85,'pickup','2022-03-02 17:30:00',16.85,2),
(3.20,'delivery','2022-03-02 18:17:00',13.25,3),
(6.3,'delivery','2022-03-06 20:32:00',24,4);


INSERT INTO pizza(OrderId,PizzaSize,PizzaCrust,PizzaState,PizzaCost,PizzaPrice)
VALUES
(1,'large','Thin','Completed',3.68,13.5),
(2,'medium','Pan','Completed',3.23,10.6),
(2,'small','Original','Completed',1.4,6.75),
(3,'large','Original','Completed',3.3,10.75),
(3,'large','Original','Completed',3.3,10.75),
(3,'large','Original','Completed',3.3,10.75),
(3,'large','Original','Completed',3.3,10.75),
(3,'large','Original','Completed',3.3,10.75),
(3,'large','Original','Completed',3.3,10.75),
(4,'x-large','Original','Completed',5.59,14.5),
(4,'x-large','Original','Completed',5.59,17),
(4,'x-large','Original','Completed',5.68,14),
(5,'x-large','Gluten-Free','Completed',7.85,16.85),
(6,'large','Thin','Completed',3.2,13.25),
(7,'large','Thin','Completed',3.75,12),
(7,'large','Thin','Completed',2.55,12);



INSERT INTO pizzatopping(PizzaId,ToppingId,ExtraTopping)
VALUES
(1,13,true),
(1,1,false),
(1,2,false),
(2,15,false),
(2,9,false),
(2,7,false),
(2,8,false),
(2,12,false),
(3,13,false),
(3,4,false),
(3,12,false),
(4,13,false),
(4,1,false),
(5,13,false),
(5,1,false),
(6,13,false),
(6,1,false),
(7,13,false),
(7,1,false),
(8,13,false),
(8,1,false),
(9,13,false),
(9,1,false),
(10,1,false),
(10,2,false),
(10,14,false),
(11,3,true),
(11,10,true),
(11,14,false),
(12,11,false),
(12,17,false),
(12,14,false),
(13,5,false),
(13,6,false),
(13,7,false),
(13,8,false),
(13,9,false),
(13,16,false),
(14,4,false),
(14,5,false),
(14,6,false),
(14,8,false),
(14,14,true),
(15,14,true),
(16,13,false),
(16,1,true);



INSERT INTO orderdiscount(OrderId,DiscountId)
VALUES
(1,3),
(4,5),
(7,1);

INSERT INTO pizzadiscount(PizzaId,DiscountId)
VALUES
(2,2),
(2,4),
(11,4),
(13,4);

INSERT INTO dinein(OrderId,TableNumber)
VALUES
(1,14),
(2,4);


INSERT INTO delivery(OrderId,CustomerAddress)
VALUES
(4,'115 Party Blvd, Anderson SC 29621'),
(6,'6745 Wessex St, Anderson SC 29621'),
(7,'8879 Suburban Home, Anderson SC 29621');

INSERT INTO pickup(OrderId)
VALUES
(3),
(5);
