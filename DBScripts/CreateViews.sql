/* SQL Script Developed By Sangamesh Itagi and Gourish Pisal*/
USE Pizzeria;
CREATE VIEW ToppingPopularity as
SELECT  topping.ToppingName as Topping , count(topping.ToppingName) + sum(pizzatopping.ExtraTopping) as  ToppingCount from pizzatopping right join topping  on pizzatopping.ToppingId=topping.ToppingId group by topping.ToppingName order by ToppingCount desc;

CREATE VIEW ProfitByPizza as
SELECT  basepizza.PizzaSize as 'Pizza Size', basepizza.PizzaCrust as 'Pizza Crust',  
sum(pizza.PizzaPrice-pizza.PizzaCost) as Profit, DATE_FORMAT(max(`order`.OrderTime), "%M %e %Y") as LastOrderDate
from basepizza right join pizza  on basepizza.PizzaSize=pizza.PizzaSize and basepizza.PizzaCrust=pizza.PizzaCrust 
right join `order` on `order`.OrderId=pizza.OrderId
group by basepizza.PizzaSize,basepizza.PizzaCrust order by profit desc;

CREATE VIEW ProfitByOrderType as
SELECT  OrderType as CustomerType, DATE_FORMAT(OrderTime,'%Y %M') as OrderMonth, SUM(OrderPrice) as TotalOrderPrice, SUM(OrderCost) as TotalOrderCost, sum(OrderPrice-OrderCost) as profit from `order` group by CustomerType,OrderMonth
union select ' ','Grand Total' as OrderMonth, sum(OrderPrice) as TotalOrderPrice ,sum(OrderCost) as TotalOrderCost, sum(OrderPrice-OrderCost) as profit from `order`;

SELECT * FROM ToppingPopularity;

SELECT * FROM ProfitByPizza;

SELECT * FROM ProfitByOrderType;
