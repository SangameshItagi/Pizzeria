//Sangamesh Itagi and Gourish Pisal
package cpsc4620;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/*
 * This file is where most of your code changes will occur You will write the code to retrieve
 * information from the database, or save information to the database
 * 
 * The class has several hard coded static variables used for the connection, you will need to
 * change those to your connection information
 * 
 * This class also has static string variables for pickup, delivery and dine-in. If your database
 * stores the strings differently (i.e "pick-up" vs "pickup") changing these static variables will
 * ensure that the comparison is checking for the right string in other places in the program. You
 * will also need to use these strings if you store this as boolean fields or an integer.
 * 
 * 
 */

/**
 * A utility class to help add and retrieve information from the database
 */

public final class DBNinja {
	private static Connection conn;

	// Change these variables to however you record dine-in, pick-up and delivery, and sizes and crusts
	public final static String pickup = "pickup";
	public final static String delivery = "delivery";
	public final static String dine_in = "dinein";

	public final static String size_s = "small";
	public final static String size_m = "medium";
	public final static String size_l = "large";
	public final static String size_xl = "x-large";

	public final static String crust_thin = "Thin";
	public final static String crust_orig = "Original";
	public final static String crust_pan = "Pan";
	public final static String crust_gf = "Gluten-Free";


	private static boolean connect_to_db() throws SQLException, IOException {

		try {
			conn = DBConnector.make_connection();
			return true;
		} catch (SQLException e) {
			return false;
		} catch (IOException e) {
			return false;
		}

	}


	public static void addOrder(Order o) throws SQLException, IOException {
		connect_to_db();
		/*
		 * add code to add the order to the DB. Remember that we're not just
		 * adding the order to the order DB table, but we're also recording
		 * the necessary data for the delivery, dinein, and pickup tables
		 */
		String[] generatedId = { "ID" };

		String insertStatement = "INSERT INTO `order`"
				+ "(OrderCost,OrderType,OrderTime,OrderPrice,CustomerId) " + "VALUES ("
				+ o.getBusPrice() + ",'" + o.getOrderType() + "','" + o.getDate() + "',"+ o.getCustPrice()+"," +
				o.getCustID() + ")";
		PreparedStatement preparedStatement = conn.prepareStatement(insertStatement, generatedId);
		int result = preparedStatement.executeUpdate();
		try {
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				o.setOrderID(resultSet.getInt(1));
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			conn.close();
		}

		if(o instanceof DineinOrder){
			addDineIn((DineinOrder)o);
		} else if(o instanceof PickupOrder){
			addPickUp((PickupOrder) o);
		} else if(o instanceof DeliveryOrder) {
			addDelivery((DeliveryOrder) o);
		}else{
			System.out.println("Invalid order type");
		}

		for(Pizza p : o.getPizzaList()) {
			p.setOrderID(o.getOrderID());
			addPizza(p);
		}
		for(Discount d : o.getDiscountList())
			useOrderDiscount(o, d);
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}

	public static void addPizza(Pizza p) throws SQLException, IOException {
		connect_to_db();
		/*
		 * Add the code needed to insert the pizza into into the database.
		 * Keep in mind adding pizza discounts to that bridge table and 
		 * instance of topping usage to that bridge table if you have't accounted
		 * for that somewhere else.
		 */
		//TODO: INSERT pizza
		String[] generatedId = { "ID" };

		String insertStatement = "insert into pizza(OrderId,PizzaSize,PizzaCrust,PizzaCost,PizzaPrice) "
				+ "values (" + p.getOrderID() + ",'" + p.getSize() + "','" + p.getCrustType()
				+ "',"  + p.getBusPrice() + "," + p.getCustPrice() + ")";
		System.out.println(insertStatement);
		PreparedStatement preparedStatement = conn.prepareStatement(insertStatement, generatedId);

		int result = preparedStatement.executeUpdate();
		System.out.println(result);
		if (result > 0) {
			ResultSet resultSet = preparedStatement.getGeneratedKeys();
			if (resultSet.next()) {
				p.setPizzaID(resultSet.getInt(1));
			}
		}
		int idx=0;
		for(Topping t : p.getToppings()) {
			useTopping(p, t, p.getIsDoubleArray()[idx++]);
		}
		for(Discount pizzaDiscount : p.getDiscounts())
			usePizzaDiscount(p, pizzaDiscount);
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}

	public static int getMaxPizzaID() throws SQLException, IOException {
		connect_to_db();
		/*
		 * A function I needed because I forgot to make my pizzas auto increment in my DB.
		 * It goes and fetches the largest PizzaID in the pizza table.
		 * You wont need to implement this function if you didn't forget to do that
		 */


		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return -1;
	}

	public static void useTopping(Pizza p, Topping t, boolean isDoubled) throws SQLException, IOException //this function will update toppings inventory in SQL and add entities to the Pizzatops table. Pass in the p pizza that is using t topping
	{
		connect_to_db();
		/*
		 * This function should 2 two things.
		 * We need to update the topping inventory every time we use t topping (accounting for extra toppings as well)
		 * and we need to add that instance of topping usage to the pizza-topping bridge if we haven't done that elsewhere
		 * Ideally, you should't let toppings go negative. If someone tries to use toppings that you don't have, just print
		 * that you've run out of that topping.
		 */
		try {
			connect_to_db();
			if((isDoubled && t.getCurINVT()<2) &&(!isDoubled && t.getCurINVT()<1)) {
				System.out.println("Sorry! We have run out of Topping.");
				throw new IOException("Sorry! We have run out of Topping.");
			}
			double updatedInventory = t.getCurINVT();
			if(isDoubled)updatedInventory-= 2;
			else updatedInventory-= 1;
			String updateStatement = "update topping set ToppingInventory="+updatedInventory+ "where ToppingId="+t.getTopID()+";";

			PreparedStatement preparedStatement= conn.prepareStatement(updateStatement);
			preparedStatement.executeUpdate();

			String insertStatement = "insert into pizzatopping(PizzaId,ToppingId,ExtraTopping)" + "values" + "("
					+ p.getPizzaID() + "," + t.getTopID() + ","+isDoubled+");";

			PreparedStatement preparedStatement2= conn.prepareStatement(insertStatement);
			preparedStatement2.executeUpdate();


		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			conn.close();
		}

		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}


	public static void usePizzaDiscount(Pizza p, Discount d) throws SQLException, IOException {
		connect_to_db();
		/*
		 * Helper function I used to update the pizza-discount bridge table. 
		 * You might use this, you might not depending on where / how to want to update
		 * this table
		 */
		String insertStatement = "insert into pizzadiscount(PizzaId,DiscountId)" + "values" + "(" + p.getPizzaID() + ","
				+ d.getDiscountID() + ")";

		PreparedStatement preparedStatement;
		try {
			connect_to_db();
			preparedStatement = conn.prepareStatement(insertStatement);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}

	public static void useOrderDiscount(Order o, Discount d) throws SQLException, IOException {
		connect_to_db();
		/*
		 * Helper function I used to update the pizza-discount bridge table. 
		 * You might use this, you might not depending on where / how to want to update
		 * this table
		 */
		String insertStatement = "insert into orderdiscount(OrderId,DiscountId)" + "values" + "(" + o.getOrderID() + ","
				+ d.getDiscountID() + ")";

		PreparedStatement preparedStatement;
		try {
			connect_to_db();
			preparedStatement = conn.prepareStatement(insertStatement);
			preparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}

		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}

	public static void addDineIn(DineinOrder d) {
		try {
			String insertStatement = "INSERT INTO dinein" + "(OrderId,TableNumber) " + "VALUES (?, ?)";

			connect_to_db();

			PreparedStatement preparedStatement = conn.prepareStatement(insertStatement);

			preparedStatement.setInt(1, d.getOrderID());
			preparedStatement.setInt(2, d.getTableNum());
			preparedStatement.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
				try {
					conn.close();
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
		}

	}

	public static void addDelivery(DeliveryOrder d) throws SQLException {
		try {
			String insertStatement = "INSERT INTO delivery" + "(OrderId,CustomerAddress) " + "VALUES (?, ?)";

			connect_to_db();

			PreparedStatement preparedStatement = conn.prepareStatement(insertStatement);

			preparedStatement.setInt(1, d.getOrderID());
			preparedStatement.setString(2, d.getAddress());
			preparedStatement.executeUpdate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			conn.close();
		}

	}

	public static void addPickUp(PickupOrder p ) throws SQLException {

		try {
			connect_to_db();
			String insertStatement = "INSERT INTO pickup" + "(OrderId) " + "VALUES (?)";

			connect_to_db();

			PreparedStatement preparedStatement = conn.prepareStatement(insertStatement);

			preparedStatement.setInt(1, p.getOrderID());
			preparedStatement.executeUpdate();
			conn.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			conn.close();
		}

	}

	public static Integer addCustomer(Customer c) throws SQLException, IOException {
		connect_to_db();
		/*
		 * This should add a customer to the database
		 */

		String insertStatement = "INSERT INTO customer(CustomerName,CustomerPhone) VALUES (?,?)";
		String[] generatedId = { "ID" };
		PreparedStatement preparedStatement = conn.prepareStatement(insertStatement, generatedId);
		preparedStatement.setString(1, c.getFName()+ " "+ c.getLName());
		preparedStatement.setString(2, c.getPhone());

		int result = preparedStatement.executeUpdate();

		if (result > 0) {
			try {
				ResultSet rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					return rs.getInt(1);
				}
			} catch (Exception e) {
				throw e;
			} finally {
				conn.close();
			}
		}

		return null;
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}


	public static void CompleteOrder(Order o) throws SQLException, IOException {
		connect_to_db();
		/*
		 * add code to mark an order as complete in the DB. You may have a boolean field
		 * for this, or maybe a completed time timestamp. However you have it.
		 */
		try {
			connect_to_db();

			String updatePizzaStatement = "update `order` set IsComplete = true where OrderId = " + o.getOrderID() + ";";

			PreparedStatement pizzaPreparedStatement = conn.prepareStatement(updatePizzaStatement);

			pizzaPreparedStatement.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}


	public static void AddToInventory(Topping t, double toAdd) throws SQLException, IOException {
		connect_to_db();
		/*
		 * Adds toAdd amount of topping to topping t.
		 */
		try {
			String updateStatement = "update topping set ToppingInventory = ToppingInventory +(?) where ToppingId = ? ;";

			PreparedStatement preparedStatement = conn.prepareStatement(updateStatement);
			preparedStatement.setDouble(1, toAdd);
			preparedStatement.setInt(2, t.getTopID());
			preparedStatement.executeUpdate();
		}  catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}


	public static void printInventory() throws SQLException, IOException {
		connect_to_db();

		/*
		 * I used this function to PRINT (not return) the inventory list.
		 * When you print the inventory (either here or somewhere else)
		 * be sure that you print it in a way that is readable.
		 * 
		 * 
		 * 
		 * The topping list should also print in alphabetical order
		 */


		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}


	public static ArrayList<Topping> getInventory() throws SQLException, IOException {
		connect_to_db();
		/*
		 * This function actually returns the toppings. The toppings
		 * should be returned in alphabetical order if you don't
		 * plan on using a printInventory function
		 */
		ArrayList<Topping> toppings = new ArrayList<Topping>();
		connect_to_db();

		try (Statement statement = conn.createStatement();
			 ResultSet resultSet = statement.executeQuery("select * from topping order by ToppingName ;")) {

			while (resultSet.next()) {
				Integer toppingId = resultSet.getInt("ToppingId");
				String toppingName = resultSet.getString("ToppingName");
				Integer currentInventoryLevel = resultSet.getInt("ToppingInventory");
				Double lgAmt = resultSet.getDouble("ToppingLargeUnits");
				Double busPrice = resultSet.getDouble("ToppingCost");
				Double custPrice = resultSet.getDouble("ToppingPrice");
				Double perAmt = resultSet.getDouble("ToppingPersonalUnits");
				Double medAmt = resultSet.getDouble("ToppingMediumUnits");
				Double xlAmt = resultSet.getDouble("ToppingXlargeUnits");

				Topping t = new Topping(toppingId, toppingName, perAmt, medAmt, lgAmt, xlAmt, custPrice, busPrice, 100, currentInventoryLevel);
				toppings.add(t);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return toppings;
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}


	public static ArrayList<Order> getCurrentOrders() throws SQLException, IOException {
		connect_to_db();
		/*
		 * This function should return an arraylist of all of the orders.
		 * Remember that in Java, we account for supertypes and subtypes
		 * which means that when we create an arrayList of orders, that really
		 * means we have an arrayList of dineinOrders, deliveryOrders, and pickupOrders.
		 * 
		 * Also, like toppings, whenever we print out the orders using menu function 4 and 5
		 * these orders should print in order from newest to oldest.
		 */
		ArrayList<Order> orders = new ArrayList<Order>();

		try {
			connect_to_db();

			String selectQuery = "select * from `order` order by OrderId desc;";

			Statement statement = conn.createStatement();

			ResultSet resultSet = statement.executeQuery(selectQuery);

			while (resultSet.next()) {
				Integer orderId = resultSet.getInt("OrderId");
				String orderType = resultSet.getString("OrderType");
				Integer customerId = resultSet.getInt("CustomerId");
				Double orderCost = resultSet.getDouble("OrderCost");
				Double orderPrice = resultSet.getDouble("OrderPrice");
				String orderTime = resultSet.getString("OrderTime");
				int isComplete = resultSet.getInt("IsComplete");

				orders.add(new Order(orderId, customerId, orderType, orderTime, orderCost, orderPrice, isComplete));

			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			conn.close();
		}

		return orders;

		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}

	public static ArrayList<Order> getCurrentOrders(String date) throws SQLException, IOException{
		connect_to_db();
		/*
		 * This function should return an arraylist of all of the orders.
		 * Remember that in Java, we account for supertypes and subtypes
		 * which means that when we create an arrayList of orders, that really
		 * means we have an arrayList of dineinOrders, deliveryOrders, and pickupOrders.
		 *
		 * Also, like toppings, whenever we print out the orders using menu function 4 and 5
		 * these orders should print in order from newest to oldest.
		 */
		ArrayList<Order> orders = new ArrayList<Order>();

		try {
			connect_to_db();

			String selectQuery = "select * from `order` where (OrderTime >= '" + date + " 00:00:00')" +
					" order by OrderId desc;";

			Statement statement = conn.createStatement();

			ResultSet resultSet = statement.executeQuery(selectQuery);

			while (resultSet.next()) {
				Integer orderId = resultSet.getInt("OrderId");
				String orderType = resultSet.getString("OrderType");
				Integer customerId = resultSet.getInt("CustomerId");
				Double orderCost = resultSet.getDouble("OrderCost");
				Double orderPrice = resultSet.getDouble("OrderPrice");
				String orderTime = resultSet.getString("OrderTime");
				int isComplete = resultSet.getInt("IsComplete");
				orders.add(new Order(orderId, customerId, orderType, orderTime, orderCost, orderPrice, isComplete));

			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			conn.close();
		}

		return orders;

		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}
	public static ArrayList<Order> sortOrders(ArrayList<Order> list) {
		/*
		 * This was a function that I used to sort my arraylist based on date.
		 * You may or may not need this function depending on how you fetch
		 * your orders from the DB in the getCurrentOrders function.
		 */


		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return null;

	}

	public static boolean checkDate(int year, int month, int day, String dateOfOrder) {
		//Helper function I used to help sort my dates. You likely wont need these


		return false;
	}


	/*
	 * The next 3 private functions help get the individual components of a SQL datetime object. 
	 * You're welcome to keep them or remove them.
	 */
	private static int getYear(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(0,4));
	}

	private static int getMonth(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(5, 7));
	}

	private static int getDay(String date)// assumes date format 'YYYY-MM-DD HH:mm:ss'
	{
		return Integer.parseInt(date.substring(8, 10));
	}


	public static double getBaseCustPrice(String size, String crust) throws SQLException, IOException {
		connect_to_db();
		double bp = 0.0;
		// add code to get the base price (for the customer) for that size and crust pizza Depending on how
		// you store size & crust in your database, you may have to do a conversion
		try (Statement statement = conn.createStatement();
			 ResultSet resultSet = statement.executeQuery("select * from basepizza where PizzaSize='"+size+"' and PizzaCrust='"+crust+"';")) {

			while (resultSet.next()) {
				bp = resultSet.getDouble("PizzaPrice");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return bp;
	}

	public static String getCustomerName(int CustID) throws SQLException, IOException {
		/*
		 *This is a helper function I used to fetch the name of a customer
		 *based on a customer ID. It actually gets called in the Order class
		 *so I'll keep the implementation here. You're welcome to change
		 *how the order print statements work so that you don't need this function.
		 */
		connect_to_db();
		String ret = "";
		String query = "Select CustomerName From customer WHERE CustomerId=" + CustID + ";";
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query);

		while(rset.next()) {
			ret = rset.getString(1);
		}
		conn.close();
		return ret;
	}

	public static double getBaseBusPrice(String size, String crust) throws SQLException, IOException {
		connect_to_db();
		double bp = 0.0;
		// add code to get the base cost (for the business) for that size and crust pizza Depending on how
		// you store size and crust in your database, you may have to do a conversion
		try (Statement statement = conn.createStatement();
			 ResultSet resultSet = statement.executeQuery("select * from basepizza where PizzaSize='"+size+"' and PizzaCrust='"+crust+"';")) {

			while (resultSet.next()) {

				bp = resultSet.getDouble("PizzaCost");

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return bp;
	}


	public static ArrayList<Discount> getDiscountList() throws SQLException, IOException {
		ArrayList<Discount> discs = new ArrayList<Discount>();
		connect_to_db();
		//returns a list of all the discounts.
		try (Statement statement = conn.createStatement();
			 ResultSet resultSet = statement.executeQuery("select * from discount;")) {

			while (resultSet.next()) {
				Integer discountId = resultSet.getInt("DiscountId");
				String discountName = resultSet.getString("DiscountName");
				Boolean isPercentDiscount = resultSet.getBoolean("IsPercent");
				Double discountAmount = resultSet.getDouble("DiscountAmount");

				discs.add(new Discount(discountId, discountName, discountAmount, isPercentDiscount));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return discs;
	}


	public static ArrayList<Customer> getCustomerList() throws SQLException, IOException {
		ArrayList<Customer> custs = new ArrayList<Customer>();
		connect_to_db();
		/*
		 * return an arrayList of all the customers. These customers should
		 *print in alphabetical order, so account for that as you see fit.
		*/

		try (Statement statement = conn.createStatement();
			 ResultSet resultSet = statement.executeQuery("select * from customer;")) {

			while (resultSet.next()) {
				Integer customerId = resultSet.getInt("CustomerId");
				String customerName = resultSet.getString("CustomerName");
				String customerPhone = resultSet.getString("CustomerPhone");
				String name[] = customerName.split(" ");
				custs.add(new Customer(customerId, name[0], name[1], customerPhone));

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}

		return custs;
		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}

	public static int getNextOrderID() throws SQLException, IOException {
		/*
		 * A helper function I had to use because I forgot to make
		 * my OrderID auto increment...You can remove it if you
		 * did not forget to auto increment your orderID.
		 */


		//DO NOT FORGET TO CLOSE YOUR CONNECTION
		return -1;
	}

	public static void printToppingPopReport() throws SQLException, IOException {
		connect_to_db();
		/*
		 * Prints the ToppingPopularity view. Remember that these views
		 * need to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * I'm not picky about how they print (other than that it should
		 * be in alphabetical order by name), just make sure it's readable.
		 */
		try {
			connect_to_db();

			Statement statement = conn.createStatement();
			String query = "SELECT * FROM ToppingPopularity;";
			ResultSet resultSet = statement.executeQuery(query);
			System.out.printf("%-20s | %-4s |%n", "Topping", "ToppingCount");
			System.out.printf("-------------------------------------\n");
			while (resultSet.next()) {

				Integer count = resultSet.getInt("ToppingCount");
				String topping = resultSet.getString("Topping");
				System.out.printf("%-20s | %-4s |%n", topping, count);

			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}

		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}

	public static void printProfitByPizzaReport() throws SQLException, IOException {
		connect_to_db();
		/*
		 * Prints the ProfitByPizza view. Remember that these views
		 * need to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * I'm not picky about how they print, just make sure it's readable.
		 */
		try {
			connect_to_db();

			Statement statement = conn.createStatement();
			String query = "SELECT * FROM ProfitByPizza;";
			ResultSet resultSet = statement.executeQuery(query);
			System.out.printf("%-15s | %-15s | %-10s| %-30s%n", "Pizza Size", "Pizza Crust", "Profit", "Last Order Date");
			System.out.printf("-----------------------------------------------------------------\n");
			while (resultSet.next()) {

				String size = resultSet.getString("Pizza Size");
				String crust = resultSet.getString("Pizza Crust");
				String lastOrderDate = resultSet.getString("LastOrderDate");
				Double profit = resultSet.getDouble("Profit");
				System.out.printf("%-15s | %-15s | %-10s| %-30s%n", size, crust, profit, lastOrderDate);

			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}

		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}

	public static void printProfitByOrderType() throws SQLException, IOException {
		connect_to_db();
		/*
		 * Prints the ProfitByOrderType view. Remember that these views
		 * need to exist in your DB, so be sure you've run your createViews.sql
		 * files on your testing DB if you haven't already.
		 * 
		 * I'm not picky about how they print, just make sure it's readable.
		 */
		try {
			connect_to_db();

			Statement statement = conn.createStatement();
			String query = "SELECT * FROM ProfitByOrderType;";
			ResultSet resultSet = statement.executeQuery(query);
			System.out.printf("%-15s | %-15s | %-18s| %-18s| %-8s%n", "Customer Type", "Order Month", "Total Order Price",
					"Total Order Cost", "Profit");
			System.out.printf("-----------------------------------------------------------------------------------\n");
			while (resultSet.next()) {

				String customerType = resultSet.getString("CustomerType");
				String orderMonth = resultSet.getString("OrderMonth");
				Double totalOrderPrice = resultSet.getDouble("TotalOrderPrice");
				Double totalOrderCost = resultSet.getDouble("TotalOrderCost");
				Double profit = resultSet.getDouble("Profit");
				System.out.printf("%-15s | %-15s | %-18s| %-18s| %-8s%n", customerType, orderMonth, totalOrderPrice,
						totalOrderCost, profit);

			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}

		//DO NOT FORGET TO CLOSE YOUR CONNECTION
	}

}