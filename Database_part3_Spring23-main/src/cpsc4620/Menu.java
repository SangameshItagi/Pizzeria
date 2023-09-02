//Sangamesh Itagi and Gourish Pisal
package cpsc4620;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;

import init.DBIniter;

/*
 * This file is where the front end magic happens.
 * 
 * You will have to write the functionality of each of these menu options' respective functions.
 * 
 * This file should need to access your DB at all, it should make calls to the DBNinja that will do all the connections.
 * 
 * You can add and remove functions as you see necessary. But you MUST have all 8 menu functions (9 including exit)
 * 
 * Simply removing menu functions because you don't know how to implement it will result in a major error penalty (akin to your program crashing)
 * 
 * Speaking of crashing. Your program shouldn't do it. Use exceptions, or if statements, or whatever it is you need to do to keep your program from breaking.
 * 
 * 
 */

public class Menu {
	public static void main(String[] args) throws SQLException, IOException {
		System.out.println("Welcome to Taylor's Pizzeria!");
		
		int menu_option = 0;

		// present a menu of options and take their selection
		
		PrintMenu();
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		DBIniter.init();
		String option = reader.readLine();
		menu_option = Integer.parseInt(option);

		while (menu_option != 9) {
			switch (menu_option) {
			case 1:// enter order
				EnterOrder();
				break;
			case 2:// view customers
				viewCustomers();
				break;
			case 3:// enter customer
				EnterCustomer();
				break;
			case 4:// view order
				// open/closed/date
				ViewOrders();
				break;
			case 5:// mark order as complete
				MarkOrderAsComplete();
				break;
			case 6:// view inventory levels
				ViewInventoryLevels();
				break;
			case 7:// add to inventory
				AddInventory();
				break;
			case 8:// view reports
				PrintReports();
				break;
			}
			PrintMenu();
			option = reader.readLine();
			menu_option = Integer.parseInt(option);
		}

	}

	public static void PrintMenu() {
		System.out.println("\n\nPlease enter a menu option:");
		System.out.println("1. Enter a new order");
		System.out.println("2. View Customers ");
		System.out.println("3. Enter a new Customer ");
		System.out.println("4. View orders");
		System.out.println("5. Mark an order as completed");
		System.out.println("6. View Inventory Levels");
		System.out.println("7. Add Inventory");
		System.out.println("8. View Reports");
		System.out.println("9. Exit\n\n");
		System.out.println("Enter your option: ");
	}

	// allow for a new order to be placed
	public static void EnterOrder() throws SQLException, IOException 
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		/*
		 * EnterOrder should do the following:
		 * Ask if the order is for an existing customer -> If yes, select the customer. If no -> create the customer (as if the menu option 2 was selected).
		 * 
		 * Ask if the order is delivery, pickup, or dinein (ask for orderType specific information when needed)
		 * 
		 * Build the pizza (there's a function for this)
		 * 
		 * ask if more pizzas should be be created. if yes, go back to building your pizza. 
		 * 
		 * Apply order discounts as needed (including to the DB)
		 * 
		 * apply the pizza to the order (including to the DB)
		 * 
		 * return to menu
		 */

		System.out.println("Is this order for an existing customer? Answer y/n:");
		Integer custId;
		double custPrice = 0;
		double busPrice = 0;
		String date = MenuHelper.getCurrentTime();
		
		//providing option for the user to choose whether it is a exisiting customer or a new customer 
		if ("y".equalsIgnoreCase(reader.readLine())) {
			System.out.println("Here's a list of current customer:");
			viewCustomers();
			System.out.println("which customer is this order for? Enter ID Number");
			custId = Integer.parseInt(reader.readLine());
		} else {
			custId = EnterCustomer();
		}
		
		//Once the customer is choosed providing option to choose the type of the order 
		
		System.out.println("Is this order for: \n1.) Dine-In\n2.) Pick-Up\n3.) Delivery\nEnter the number of your choice:");
		
		Integer orderTypeInt = Integer.parseInt(reader.readLine());
		Order order = null;
		switch(orderTypeInt) {
			case 1:
				System.out.println("Enter the table number  ");
				Integer tablenum = Integer.parseInt(reader.readLine());
				order = new DineinOrder(0, custId, date, custPrice, busPrice, 0, tablenum);
			break;
			case 2:
				order = new PickupOrder(0, custId, date, custPrice, busPrice, 0, 0);
			break;
			case 3:
				System.out.println("Enter the customer address ");
				String customerAddress = reader.readLine();
				order = new DeliveryOrder(0, custId, date, custPrice, busPrice, 0, customerAddress);
			break;
		}

		System.out.println("Let's build a pizza!"); 
		boolean addMorePizza = true;
		while(addMorePizza) {
			Pizza pizza = buildPizza();
			busPrice += pizza.getBusPrice();
			custPrice += pizza.getCustPrice();
			order.addPizza(pizza);
			System.out.println("Do you want to add another pizza : type y/n:");
			if("y".equalsIgnoreCase(reader.readLine())) addMorePizza = true;
			else addMorePizza = false;
		}
		
		order.setCustPrice(custPrice);
		order.setBusPrice(busPrice);
		
		helperOrderDiscount(order);
		
		DBNinja.addOrder(order);

		System.out.println("Finished adding order...Returning to menu...");
	}
	
	private static void helperOrderDiscount(Order order) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Do you want to add order Discount : type y/n:");
		try {
			Boolean addorderDiscount = "y".equals(reader.readLine());
			while (addorderDiscount) {

				ArrayList<Discount> discounts = DBNinja.getDiscountList();
				discounts.forEach(d->System.out.println(d.toString()));
				
				System.out.println("Please enter a discount id or -1 if you don't want to add a discount");
				
				Integer discountId = Integer.parseInt(reader.readLine());
				
				if (discountId == -1) break;
				Discount pizzaDiscount = discounts.stream().filter(d->d.getDiscountID()==discountId).findAny().get();
				order.addDiscount(pizzaDiscount);
				
				System.out.println("Do you want to add another discount : type y/n:");
				if("y".equalsIgnoreCase(reader.readLine())) addorderDiscount = true;
				else addorderDiscount = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void helperPizzaDiscount(Pizza pizza) {
		//adding discount
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try{
		System.out.println("Do you want to add pizza Discount : type y/n:");
		Boolean addDiscount = "y".equalsIgnoreCase(reader.readLine());
		while (addDiscount) {
	
			ArrayList<Discount> discounts = DBNinja.getDiscountList();
			
			for (Discount discount : discounts) {
				System.out.println(discount.toString());
			}
			
			System.out.println("Please enter a discount id or -1 if you don't want to add a discount");
			
			Integer discountId = Integer.parseInt(reader.readLine());
			
			if (discountId==-1) break;
			Discount pizzaDiscount = discounts.stream().filter(d->d.getDiscountID()==discountId).findAny().get();
			pizza.addDiscounts(pizzaDiscount);
			
			System.out.println("Do you want to add another discount : type y/n:");
			if("y".equalsIgnoreCase(reader.readLine())) addDiscount = true;
			else addDiscount = false;

		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void helperToppings(Pizza pizza) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		Boolean addMoreTopping = true;
			
		while(addMoreTopping) {
			try {		
				ArrayList<Topping> toppings = ViewInventoryLevels();
				
				System.out.println("Please enter a topping id from the above list if toppings is not required enter -1:");
				
				Integer toppingId = Integer.parseInt(reader.readLine());
				
				if (toppingId==-1) {
					break;
				}
				Topping topping = toppings.stream().filter(t->t.getTopID()==toppingId).findAny().get();
				boolean isExtra = false;
				System.out.println("Do you want add the same toping as extra too : type y/n:");
		
				isExtra = "y".equalsIgnoreCase(reader.readLine());
				pizza.addToppings(topping, isExtra);

				System.out.println("Do you want to add another topping : type y/n:");
				if("y".equalsIgnoreCase(reader.readLine())) addMoreTopping = true;
				else addMoreTopping = false;
			}
			catch (Exception e) {
				e.printStackTrace();
			}	

		}
	}
	
	public static void viewCustomers()
	{
		/*
		 * Simply print out all of the customers from the database. 
		 */
		try {
			ArrayList<Customer> customers = DBNinja.getCustomerList();
			customers.forEach(c -> System.out.println(c));
		}
		catch (Exception e) {
			System.out.println("There is an error in displaying the cusotmers " + e);
			e.printStackTrace();
		}
	}
	

	// Enter a new customer in the database
	public static Integer EnterCustomer() throws SQLException, IOException 
	{
		/*
		 * Ask what the name of the customer is. YOU MUST TELL ME (the grader) HOW TO FORMAT THE FIRST NAME, LAST NAME, AND PHONE NUMBER.
		 * If you ask for first and last name one at a time, tell me to insert First name <enter> Last Name (or separate them by different print statements)
		 * If you want them in the same line, tell me (First Name <space> Last Name).
		 * 
		 * same with phone number. If there's hyphens, tell me XXX-XXX-XXXX. For spaces, XXX XXX XXXX. For nothing XXXXXXXXXXXX.
		 * 
		 * I don't care what the format is as long as you tell me what it is, but if I have to guess what your input is I will not be a happy grader
		 * 
		 * Once you get the name and phone number (and anything else your design might have) add it to the DB
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Please Enter the customer name (First Name <Space> Last Name):");

		String customerName = reader.readLine();
		
		String[] name = customerName.split(" ");
		if (name.length<2)
			throw new IllegalArgumentException("Please enter full name: " + customerName);
		String firstName = name[0];
		String lastName = name[1];
		
		System.out.println("What is this customer's phone number (XXX-XXX-XXXX) (No dash/space):");
		
		String phoneNumber = reader.readLine();
		
		Customer customer = new Customer(0, firstName, lastName, phoneNumber);
		try {
			return DBNinja.addCustomer(customer);
		}
		catch (SQLException | IOException e) {
			System.out.println("There is an error while adding the customer " + e);
			throw e;
		}
	}

	// View any orders that are not marked as completed
	public static void ViewOrders() throws SQLException, IOException 
	{
	/*
	 * This should be subdivided into two options: print all orders (using simplified view) and print all orders (using simplified view) since a specific date.
	 * 
	 * Once you print the orders (using either sub option) you should then ask which order I want to see in detail
	 * 
	 * When I enter the order, print out all the information about that order, not just the simplified view.
	 * 
	 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			
		System.out.println("Would you like to:\n(a) display all orders\n(b) display orders since a specific date");
		
		String selectedOption = reader.readLine();
		try {
			ArrayList<Order> orders = new ArrayList<>();
			if ("a".equals(selectedOption)) {
				orders = DBNinja.getCurrentOrders();
			} else {
				System.out.println("What is the date you want to restrict by? (FORMAT= YYYY-MM-DD)");
				String date = reader.readLine();
				orders = DBNinja.getCurrentOrders(date);
			}
			
			orders.forEach(c -> System.out.println(c.toSimplePrint()));
			System.out.println("Which order would you like to see in detail? Enter the number:");
			Integer orderId = Integer.parseInt(reader.readLine());
			Order order = orders.stream().filter(o -> o.getOrderID()==orderId)
            		.findFirst().orElse(null);
			System.out.println(order.toString());
		}
		catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	
	// When an order is completed, we need to make sure it is marked as complete
	public static void MarkOrderAsComplete() throws SQLException, IOException 
	{
		/*All orders that are created through java (part 3, not the 7 orders from part 2) should start as incomplete
		 * 
		 * When this function is called, you should print all of the orders marked as complete 
		 * and allow the user to choose which of the incomplete orders they wish to mark as complete
		 * 
		 */
		ArrayList<Order> orders = DBNinja.getCurrentOrders();
		for (Order order : orders) {
			System.out.println(order.toSimplePrint());
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		
		System.out.println("Enter the order Id you wish to mark as completed :");
		Integer orderId = Integer.parseInt(reader.readLine());
		DBNinja.CompleteOrder(orders.stream().filter(o->o.getOrderID()==orderId).findFirst().get());
	}

	// See the list of inventory and it's current level
	public static ArrayList<Topping> ViewInventoryLevels() throws SQLException, IOException 
	{
		//print the inventory. I am really just concerned with the ID, the name, and the current inventory
		ArrayList<Topping> toppings = DBNinja.getInventory();		
		try {
			System.out.printf("%-9s %-25s %9s%n", "ID", "Name", "CurINVT");
			toppings.forEach(topping -> System.out.printf("%-9s %-25s %9s%n", topping.getTopID(), topping.getTopName(),
						topping.getCurINVT()));
				
		} catch (Exception e) {
			System.out.println("There is an error while viewing Inventory levels " + e);
			throw e;
		}
		return toppings;
	}

	// Select an inventory item and add more to the inventory level to re-stock the
	// inventory
	public static void AddInventory() throws SQLException, IOException 
	{
		/*
		 * This should print the current inventory and then ask the user which topping they want to add more to and how much to add
		 */
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			ViewInventoryLevels();
			System.out.println("Which topping do you want to add inventory to? Enter the number:");
			Integer toppingId = Integer.parseInt(reader.readLine());
	
			System.out.println("How many units would you like to add?");
			
			Double amountToAdd = Double.parseDouble(reader.readLine());
			ArrayList<Topping> toppings = DBNinja.getInventory();
			Topping topping = toppings.stream().filter(t->t.getTopID()==toppingId).findFirst().orElse(null);
			DBNinja.AddToInventory(topping, amountToAdd);
		} catch (Exception e) {
			throw e;
		}
	}

	// A function that builds a pizza. Used in our add new order function
	public static Pizza buildPizza() throws SQLException, IOException
	{
		
		/*
		 * This is a helper function for first menu option.
		 * 
		 * It should ask which size pizza the user wants and the crustType.
		 * 
		 * Once the pizza is created, it should be added to the DB.
		 * 
		 * We also need to add toppings to the pizza. (Which means we not only need to add toppings here, but also our bridge table)
		 * 
		 * We then need to add pizza discounts (again, to here and to the database)
		 * 
		 * Once the discounts are added, we can return the pizza
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("What size is the pizza? \n1.) Small\n2.) Medium\n3.) Large\n4.) X-Large \nEnter the corresponding number:");
		Integer pizzaSizeMenuid = Integer.parseInt(reader.readLine());
		String pizzaSize = MenuHelper.getPizzaSize(pizzaSizeMenuid);

		System.out.println("What crust for this pizza : \n1.) Thin\n2.) Original\n3.) Pan\n4.) Gluten-Free \nEnter the corresponding number:");
		Integer pizzaCrustMenuid = Integer.parseInt(reader.readLine());
		String pizzaCrust = MenuHelper.getPizzaCrust(pizzaCrustMenuid);

		String pizzaTimeStamp = MenuHelper.getCurrentTime();
		double pizzaCustPrice = DBNinja.getBaseCustPrice(pizzaSize, pizzaCrust);
		double pizzaBusPrice = DBNinja.getBaseBusPrice(pizzaSize, pizzaCrust);
		Pizza pizza = new Pizza(0, pizzaSize, pizzaCrust, 0, "PREPARING", pizzaTimeStamp, pizzaCustPrice, pizzaBusPrice);

		helperToppings(pizza);
		helperPizzaDiscount(pizza);
		//System.out.println(pizza.toString());
		return pizza;
	}
	
	private static int getTopIndexFromList(int TopID, ArrayList<Topping> tops)
	{
		/*
		 * This is a helper function I used to get a topping index from a list of toppings
		 * It's very possible you never need to use a function like this
		 * 
		 */
		int ret = -1;
		
		
		
		return ret;
	}
	
	
	public static void PrintReports() throws SQLException, NumberFormatException, IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		/*
		 * This function calls the DBNinja functions to print the three reports.
		 * 
		 * You should ask the user which report to print
		 */
		System.out.println("Which report do you wish to print? Enter\n1.) ToppingPopularity\n2.) ProfitByPizza\n3.) ProfitByOrderType");

		switch(Integer.parseInt(reader.readLine())) {
			case 1:
				DBNinja.printToppingPopReport();
				break;
			case 2:
				DBNinja.printProfitByPizzaReport();
				break;
			case 3:
				DBNinja.printProfitByOrderType();
				break;
		}
	}

}


//Prompt - NO CODE SHOULD TAKE PLACE BELOW THIS LINE
//DO NOT EDIT ANYTHING BELOW HERE, I NEED IT FOR MY TESTING DIRECTORY. IF YOU EDIT SOMETHING BELOW, IT BREAKS MY TESTER WHICH MEANS YOU DO NOT GET GRADED (0)

/*
CPSC 4620 Project: Part 3 â€“ Java Application Due: Thursday 11/30 @ 11:59 pm 125 pts

For this part of the project you will complete an application that will interact with your database. Much of the code is already completed, you will just need to handle the functionality to retrieve information from your database and save information to the database.
Note, this program does little to no verification of the input that is entered in the interface or passed to the objects in constructors or setters. This means that any junk data you enter will not be caught and will propagate to your database, if it does not cause an exception. Be careful with the data you enter! In the real world, this program would be much more robust and much more complex.

Program Requirements:

Add a new order to the database: You must be able to add a new order with pizzas to the database. The user interface is there to handle all of the input, and it creates the Order object in the code. It then calls DBNinja.addOrder(order) to save the order to the database. You will need to complete addOrder. Remember addOrder will include adding the order as well as the pizzas and their toppings. Since you are adding a new order, the inventory level for any toppings used will need to be updated. You need to check to see if there is inventory available for each topping as it is added to the pizza. You can not let the inventory level go negative for this project. To complete this operation, DBNinja must also be able to return a list of the available toppings and the list of known customers, both of which must be ordered appropropriately.

View Customers: This option will display each customer and their associated information. The customer information must be ordered by last name, first name and phone number. The user interface exists for this, it just needs the functionality in DBNinja

Enter a new customer: The program must be able to add the information for a new customer in the database. Again, the user interface for this exists, and it creates the Customer object and passes it to DBNinja to be saved to the database. You need to write the code to add this customer to the database. You do need to edit the prompt for the user interface in Menu.java to specify the format for the phone number, to make sure it matches the format in your database.

View orders: The program must be able to display orders and be sorted by order date/time from most recent to oldest. The program should be able to display open orders, all the completed orders or just the completed order since a specific date (inclusive) The user interface exists for this, it just needs the functionality in DBNinja

Mark an order as completed: Once the kitchen has finished prepping an order, they need to be able to mark it as completed. When an order is marked as completed, all of the pizzas should be marked as completed in the database. Open orders should be sorted as described above for option #4. Again, the user interface exists for this, it just needs the functionality in DBNinja

View Inventory Levels: This option will display each topping and its current inventory level. The toppings should be sorted in alphabetical order. Again, the user interface exists for this, it just needs the functionality in DBNinja

Add Inventory: When the inventory level of an item runs low, the restaurant will restock that item. When they do so, they need to enter into the inventory how much of that item was added. They will select a topping and then say how many units were added. Note: this is not creating a new topping, just updating the inventory level. Make sure that the inventory list is sorted as described in option #6. Again, the user interface exists for this, it just needs the functionality in DBNinja

View Reports: The program must be able to run the 3 profitability reports using the views you created in Part 2. Again, the user interface exists for this, it just needs the functionality in DBNinja

Modify the package DBConnector to contain your database connection information, this is the same information you use to connect to the database via MySQL Workbench. You will use DBNinja.connect_to_db to open a connection to the database. Be aware of how many open database connections you make and make sure the database is properly closed!
Your code needs to be secure, so any time you are adding any sort of parameter to your query that is a String, you need to use PreparedStatements to prevent against SQL injections attacks. If your query does not involve any parameters, or if your queries parameters are not coming from a String variable, then you can use a regular Statement instead.

The Files: Start by downloading the starter code files from Canvas. You will see that the user interface and the java interfaces and classes that you need for the assignment are already completed. Review all these files to familiarize yourself with them. They contain comments with instructions for what to complete. You should not need to change the user interface except to change prompts to the user to specify data formats (i.e. dashes in phone number) so it matches your database. You also should not need to change the entity object code, unless you want to remove any ID fields that you did not add to your database.

You could also leave the ID fields in place and just ignore them. If you have any data types that donâ€™t match (i.e. string size options as integers instead of strings), make the conversion when you pull the information from the database or add it to the database. You need to handle data type differences at that time anyway, so it makes sense to do it then instead of making changes to all of the files to handle the different data type or format.

The Menu.java class contains the actual user interface. This code will present the user with a menu of options, gather the necessary inputs, create the objects, and call the necessary functions in DBNinja. Again, you will not need to make changes to this file except to change the prompt to tell me what format you expect the phone number in (with or without dashes).

There is also a static class called DBNinja. This will be the actual class that connects to the database. This is where most of the work will be done. You will need to complete the methods to accomplish the tasks specified.

Also in DBNinja, there are several public static strings for different crusts, sizes and order types. By defining these in one place and always using those strings we can ensure consistency in our data and in our comparisons. You donâ€™t want to have â€œSMALLâ€� â€œsmallâ€� â€œSmallâ€� and â€œPersonalâ€� in your database so it is important to stay consistent. These strings will help with that. You can change what these strings say in DBNinja to match your database, as all other code refers to these public static strings.

Start by changing the class attributes in DBConnector that contain the data to connect to the database. You will need to provide your database name, username and password. All of this is available is available in the Chapter 15 lecture materials. Once you have that done, you can begin to build the functions that will interact with the database.

The methods you need to complete are already defined in the DBNinja class and are called by Menu.java, they just need the code. Two functions are completed (getInventory and getTopping), although for a different database design, and are included to show an example of connecting and using a database. You will need to make changes to these methods to get them to work for your database.

Several extra functions are suggested in the DBNinja class. Their functionality will be needed in other methods. By separating them out you can keep your code modular and reduce repeated code. I recommend completing your code with these small individual methods and queries. There are also additional methods suggested in the comments, but without the method template that could be helpful for your program. HINT, make sure you test your SQL queries in MySQL Workbench BEFORE implementing them in codeâ€¦it will save you a lot of debugging time!

If the code in the DBNinja class is completed correctly, then the program should function as intended. Make sure to TEST, to ensure your code works! Remember that you will need to include the MySQL JDBC libraries when building this application. Otherwise you will NOT be able to connect to your database.

Compiling and running your code: The starter code that will compile and â€œrunâ€�, but it will not do anything useful without your additions. Because so much code is being provided, there is no excuse for submitting code that does not compile. Code that does not compile and run will receive a 0, even if the issue is minor and easy to correct.

Help: Use MS Teams to ask questions. Do not wait until the last day to ask questions or get started!

Submission You will submit your assignment on Canvas. Your submission must include: â€¢ Updated DB scripts from Part 2 (all 5 scripts, in a folder, even if some of them are unchanged). â€¢ All of the class code files along with a README file identifying which class files in the starter code you changed. Include the README even if it says â€œI have no special instructions to shareâ€�. â€¢ Zip the DB Scripts, the class files (i.e. the application), and the README file(s) into one compressed ZIP file. No other formats will be accepted. Do not submit the lib directory or an IntellJ or other IDE project, just the code.

Testing your submission Your project will be tested by replacing your DBconnector class with one that connects to a special test server. Then your final SQL files will be run to recreate your database and populate the tables with data. The Java application will then be built with the new DBconnector class and tested.

No late submissions will be accepted for this assignment.*/

