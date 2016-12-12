package OrderTracker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.event.*;

import javax.swing.*;
import javax.swing.border.*;

public class LoginScreen extends JFrame {

	private static final long serialVersionUID = 1L;

	private final JPanel contentPane = new JPanel(),	// the main panel to contain every interface
		                 loginPanel = new JPanel(),		// for the login window
		                 entryPanel = new JPanel(),		// to hold the text fields in the login window
		                 textPanel = new JPanel(),		// to hold the user and pass fields
		                 userPanel = new JPanel(),		// to hold the username entry field in login
		                 passPanel = new JPanel(),		// to hold the password entry field in login
		                 buttonPanel = new JPanel(),	// to hold the buttons in the login
		                 restaurantPanel = new JPanel(),// for the menu window
		                 menuPanel = new JPanel(),		// for items and infoPanel
		                 greetingPanel = new JPanel(),	// for the greeting bar in the menu window
		                 allItemsTab = new JPanel(),	//	both food and drink items tab
		                 foodTab = new JPanel(),		// for the food items tab
		                 drinkTab = new JPanel(),		// for the drink items tab
		                 infoPanel = new JPanel(),		// to hold the item info to the right
		                 itemListPanel = new JPanel(),
		                 itemDetailsPanel = new JPanel(),
		                 descPanel = new JPanel(),
		                 cardEntryPanel = new JPanel();
	
	private JList menuList,
			      foodList,
			      drinkList;

	private final JTabbedPane itemsPanel = new JTabbedPane();
	
	private final JScrollPane menuScroll = new JScrollPane(), 
						      foodScroll = new JScrollPane(),
						      drinkScroll = new JScrollPane();
	
	private final JTextField userField = new JTextField(),
			                 cardNumField = new JTextField();
	
	private final JPasswordField passField = new JPasswordField();
	
	private final JLabel userLabel = new JLabel("Username"),
	                     passLabel = new JLabel("Password"),
	                     incorrectLogin = new JLabel(""),
	                     greetingLabel = new JLabel("Hello", SwingConstants.CENTER),
	    	             welcomeLabel = new JLabel(
	    	            		 "Welcome to the Restaurant Order Tracking System!",
	    	            		 SwingConstants.CENTER);
	
	private final JLabel price = new JLabel(),
			             rating = new JLabel(),
			             calories = new JLabel(),
			             is = new JLabel(),
			             type = new JLabel(),
						 itemLabel = new JLabel(
								 "Click an item to view its details!", SwingConstants.CENTER);

    private final JButton submit = new JButton("Login"),
    		              clear = new JButton("Clear"),
    		              cancel = new JButton("Exit"),
    		              logout = new JButton("Logout"),
    		              order = new JButton("Order Item");
   
    private CardLayout cl = new CardLayout();
    
	private SQLQuerier query = new SQLQuerier("Restaurant");
    
    private int borderSize = 20;
    private boolean isEmployee = true;
    
    private String[] tabNames =
    	             	{"All Items", "Order a Food", "Order a Drink"},
    	             windowNames =
    	             	{"Login", "Select an item to order", "Order"};
    
    private String currentUser, fname, lname, personid;
    
    private float itemprice;
    private String itemname;
    		
	/****************************
	 ** Launch the application **
	 ****************************/
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginScreen frame = new LoginScreen();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	/******************************************
	 ** Create the frame for the application **
	 ******************************************/
	
	public LoginScreen() {
		createLoginWindow(this);
	}
	
	public void createLoginWindow(LoginScreen ls) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		// create the main window
		contentPane.setBorder(
				new EmptyBorder(borderSize, borderSize, borderSize, borderSize));
		setContentPane(contentPane);
		contentPane.setLayout(cl);	// set the main window to a card layout
		
		// create each main interface of the program
		createLoginPanel();	// login screen
		createMenuPanel();	// menu screen

		// add each interface to the primary content pane
		contentPane.add(loginPanel, "1");
		contentPane.add(restaurantPanel, "2");
		
		// show the login screen first
		switchToCard(1);
		
		// add action listeners for all of the buttons and fields
		createActionListeners(ls);
		
		// condense and center the window before displaying
        pack();
        setLocationRelativeTo(null);
        
        // removes the incorrect login text
		incorrectLogin.setVisible(false);
        
	}
	
	// Switch to a different card using the CardLayout
	public void switchToCard(int card) {
        setTitle(windowNames[card - 1]);
		cl.show(contentPane, "" + card);
	}
	
	
	/*********************************************************
	 ** Methods for creating and processing the Login panel **
	 *********************************************************/
	
	public void createLoginPanel() {
		// create a new panel with a border layout
		loginPanel.setLayout(new BorderLayout());
		
		// create a panel with entry fields
		entryPanel.setLayout(new FlowLayout());
		
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
		
		// align and size the user textfield
		userField.setHorizontalAlignment(SwingConstants.LEFT);
		userField.setColumns(15);
		
		// add label and textfield to the username panel
		userPanel.add(userLabel);
		userPanel.add(userField);
		
		// align and size the password field
		passField.setHorizontalAlignment(SwingConstants.LEFT);
		passField.setColumns(15);
			
		// add label and passwordfield to the password panel
		passPanel.add(passLabel);
		passPanel.add(passField);
		
		// create and add a button panel with login and cancel
		buttonPanel.add(cancel);	
		buttonPanel.add(clear);
		buttonPanel.add(submit);
		
		// add the username, password, and button panels to the entryPanel
		textPanel.add(userPanel);
		textPanel.add(passPanel);
		entryPanel.add(textPanel);
		entryPanel.add(buttonPanel);
		
		// add the welcome message to the top
		welcomeLabel.setBorder(new EmptyBorder(0, 0, borderSize/2, 0));
		loginPanel.add(welcomeLabel, BorderLayout.NORTH);
		
		// add the entry panel to the center of the main border layout panel
		loginPanel.add(entryPanel, BorderLayout.CENTER);
		
		// add panel for displaying the incorrect login message at the bottom
		loginPanel.add(incorrectLogin, BorderLayout.SOUTH);
	}

	public boolean lookupLogin(String table, String username, String password) {
		
		// create the column prefix needed for customers or employees
		String prefix = (table.equals("Customers")) ? "c" : "e";
		
		// flag to say whether the login info was found
		boolean valid = false;
		
		// string arrays to hold the username and password combo
		String loginInfo[] = {username, password};
		String columns[] = {prefix + "username", prefix + "password"};
		
		// search for the login info in the database
		if(query.searchFor(table, columns, loginInfo)) {
			valid = true;
		}
		
		// return whether the login info was found or not
		return valid;
	}
	
	public void processLogin() {
		
		String greeting;
		
		// check the customers table for the login info
		if(lookupLogin("Customers", userField.getText(), new String(passField.getPassword()))) {
			
			isEmployee = false;
			
			currentUser = userField.getText();	// store the username of this user
			
			getUserInfo("Customers", currentUser);
			greetingLabel.setText(formatGreeting("Customer"));
			logout.setText("Logout " + currentUser);
			
			switchToCard(2);					// go to menu screen
			
	        pack();
		}
		// check the employees table for the login info
		else if(lookupLogin("Employees", userField.getText(), new String(passField.getPassword()))) {
			
			isEmployee = true;
			
			currentUser = userField.getText();	// store the username of this user
			
			getUserInfo("Employees", currentUser);
			greetingLabel.setText(formatGreeting("Employee"));
			logout.setText("Logout " + currentUser);
			
			switchToCard(2);	// go to menu screen
			
	        pack();
		}
		// show text saying the login info was incorrect
		else {
			incorrectLogin.setVisible(false);
			incorrectLogin.setForeground(Color.RED);
			incorrectLogin.setText("Incorrect Username or Password.");
			incorrectLogin.setVisible(true);
	        pack();
		}

	}
	
	// create the welcome greeting in the menu screen
	private String formatGreeting(String type) {
		String greeting;

		// formatting the name to a proper case
		greeting = "Hello " + fname.substring(0, 1).toUpperCase()
				+ fname.substring(1).toLowerCase() + "! " +
				"(" + type + " #" + personid + ")";
		
		return greeting;
	}
	
	// retrieve the current user's information
	private void getUserInfo(String table, String user) {
		
		// search the database for the user's information
		String[] pidfnln = query.getRowItems(
				"People natural join " + table,
				((table.equals("Customers")) ? "c" : "e") + "username",
				user, new String[]{"personid", "fname", "lname"});

		// store the inormation as variables
		personid = pidfnln[0];
		fname = pidfnln[1];
		lname = pidfnln[2];
		
	}
	
	// reset any fields and labels that use the user's info.
	private void resetUserLoginInfo() {
		
		// reset stored user info
		currentUser = fname = lname = personid = "";
		
		// reset stored item information
		itemprice = 0;
		itemname = "";
		
		// reset all text fields
		greetingLabel.setText("");
		userField.setText("");
		passField.setText("");
		itemLabel.setText("Click an item to view its details!");
		
		// hide item related panels
		descPanel.setVisible(false);
		order.setVisible(false);
	}
	
	
	/*****************************************
	 ** Methods for creating the Menu Panel **
	 *****************************************/
	
	private void createMenuPanel() {
			
		// set layouts for all panels
		restaurantPanel.setLayout(new BorderLayout());	// to hold the entire user interface
		greetingPanel.setLayout(new BorderLayout());	// to hold the greeting message and logout button
		menuPanel.setLayout(new GridLayout(1, 2));		// to hold the tabbed menu and the info panel
		infoPanel.setLayout(new BorderLayout());		// to hold the selected item's info and order button
		descPanel.setLayout(new BorderLayout());		// to hold the item details and card entry fields
		itemDetailsPanel.setLayout(new BoxLayout(itemDetailsPanel, BoxLayout.PAGE_AXIS));	// to hold the item's descriptive info
		cardEntryPanel.setLayout(new BoxLayout(cardEntryPanel, BoxLayout.Y_AXIS));	// to hold the textbox to input a card number
		
		// add proper spacing for the greeting panel
		greetingPanel.setBorder(new EmptyBorder(0, 0, borderSize, 0));
		
		// add the greeting and the logout button to the greeting panel
		greetingPanel.add(greetingLabel, BorderLayout.CENTER);
		greetingPanel.add(logout, BorderLayout.EAST);
		
		// create the tabs for the item lists
		itemsPanel.addTab(tabNames[0], allItemsTab);
	    itemsPanel.addTab(tabNames[1], foodTab);
	    itemsPanel.addTab(tabNames[2], drinkTab);
	    
		// add all items to each of the lists
		menuList = populateJList(query.getListOf("MenuItems", "itemname"));
	    foodList = populateJList(query.getListOf("FoodItems", "itemname"));
	    drinkList = populateJList(query.getListOf("DrinkItems", "itemname"));
	    
	    // make a scrollbar for each list
		menuScroll.setViewportView(menuList);
		foodScroll.setViewportView(foodList);
		drinkScroll.setViewportView(drinkList);
		
	    // add each scrolling list to their respective tabs
		allItemsTab.add(menuScroll, BorderLayout.WEST);
		foodTab.add(foodScroll, BorderLayout.WEST);
		drinkTab.add(drinkScroll, BorderLayout.WEST);
		
		// set the selection modes for each list
		menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		foodList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		drinkList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//// Adding the right side info panel
		itemLabel.setBorder(new EmptyBorder(0, 0, borderSize/2, 0));
		
		// adding text labels to the description panel
		itemDetailsPanel.add(price);
		itemDetailsPanel.add(rating);
		itemDetailsPanel.add(calories);
		itemDetailsPanel.add(is);
		itemDetailsPanel.add(type);
		
		// add a label and card text field to the card panel
		cardEntryPanel.add(new JLabel("Enter your Card Number:"));
		cardEntryPanel.add(cardNumField);
		cardNumField.setMaximumSize(
			     new Dimension(Integer.MAX_VALUE, cardNumField.getPreferredSize().height));
		
		// add the item detals and card entry to the description panel
		descPanel.add(itemDetailsPanel, BorderLayout.NORTH);
		descPanel.add(cardEntryPanel, BorderLayout.SOUTH);
		descPanel.setVisible(false);
		
		// add info description and order button
		infoPanel.add(itemLabel, BorderLayout.NORTH);
		infoPanel.add(descPanel, BorderLayout.CENTER);
		infoPanel.add(order, BorderLayout.SOUTH);
		infoPanel.setBorder(new EmptyBorder(borderSize/2, borderSize/2, borderSize/2, borderSize/2));
		order.setVisible(false);
		
		//// Adding all panels to the main window
		itemListPanel.add(itemsPanel);
		
		// add (greeting and food/drink panels) to main menu panel
		menuPanel.add(itemListPanel);
		menuPanel.add(infoPanel);
	
		// add the greeting panel and the menu panel to the window
		restaurantPanel.add(greetingPanel, BorderLayout.NORTH);
		restaurantPanel.add(menuPanel, BorderLayout.SOUTH);
		
	}
	
	// add items of a string array to a JList
	public JList<String> populateJList(String[] list) {
		
		// create a list model and add all items in the string array
		DefaultListModel<String> model = new DefaultListModel<String>();
		for (int i=0, n = list.length; i<n; i++)
			model.addElement(list[i]);

		// return a JList using the model
		return new JList<String>(model);
	}
	
	// method for updating the info panel with item information
	public void updateItemInfo(String itemname) {
		
		// hide the panel before updating it
		descPanel.setVisible(false);
		order.setVisible(false);
		
		// get the info from the database as a string array
		String[] info = getItemInfo("MenuItems", itemname);
		
		// store the price and itemname directly
		itemprice = Float.parseFloat(info[0]);
		this.itemname = itemname;

		// set the text of their respective jlabel with the updated information
		price.setText("Price: $" + info[0]);
		rating.setText("Rating: " + info[1] + "/5");
		calories.setText("Calories: " + info[2]);
		
		// check if the item was vegetarian or diet and show labels as appropriate
		if(query.searchFor("FoodItems", "itemname", itemname)){
			if(getItemInfo("FoodItems", itemname)[0].equals("1")) {
				is.setText("VEGETARIAN");
				is.setVisible(true);
			}
			else {
				is.setVisible(false);
			}
			type.setVisible(false);
		}
		else {
			String[] extrainfo = getItemInfo("DrinkItems", itemname);

			if(extrainfo[0].equals("1")) {
				is.setText("DIET");
				is.setVisible(true);
			}
			else {
				is.setVisible(false);
			}
			type.setText(extrainfo[1]);
			type.setVisible(true);
		}
		
		// put the itemname in the order button
		order.setText("Order " + itemname);

		// update and show the panel
		itemDetailsPanel.repaint();
		descPanel.setVisible(true);
		order.setVisible(true);
	}
	
	// obtains the extra data for an item from the specified table
	public String[] getItemInfo(String table, String itemname) {
		
		// create an empty string array
		String[] columns = {""};
		
		// set the string array depending on the type of table being retrieved
		switch(table) {
			case "MenuItems":
				// get the price, rating, and calorie count from the menu table
				columns = new String[]{"price", "rating", "calories"};
				break;
			case "FoodItems":
				// check if the food is vegetarian from the food table
				columns = new String[]{"isvegetarian"};
				break;
			case "DrinkItems":
				// check if the drink is diet or if it is alcoholic from the drink table
				columns = new String[]{"isdiet", "drinktype"};
				break;
			default:
				return null;
		}
		
		// make the query
		return query.getRowItems(
				table,
				"itemname",
				itemname,
				columns);
	}
	
	public boolean checkCardValidity(String id, String card) {
		// flag to say whether the login info was found
		boolean valid = false;
		
		// string arrays to hold the id and cardnumber combo
		String userCard[] = {id, card};
		String columns[] = {"personid", "cardnumber"};
		
		// search for the login info in the database
		if(query.searchFor("People", columns, userCard)) {
			valid = true;
		}
		
		// return whether the login info was found or not
		return valid;
	}
	
	// adds a order to the database
	public void addOrderToDatabase(String cardnumber){

		// create a decimal format for the price
		DecimalFormat df = new DecimalFormat("###.00");
		
		// format the values to fit into a query
		String values =
				(query.countRowsIn("Orders") + 1) + ", " +
				Integer.parseInt(cardnumber) + ", '" + 
				itemname + "', " + 
				df.format(calculatePriceTotal()) + ", " + 
				"DATETIME('now')";
		
		// make the update to the database
		query.addRowTo("Orders", values);
	}
	
	// calculates the combined total for an order after a discount
	public float calculatePriceTotal() {
		float total = itemprice;
		int percent = 0;
		
		// check if the user is an employee
		if(isEmployee) {
			// get the percent of the employee's discount from the database
			percent = Integer.parseInt(query.getRowItems(
					"Employees",
					"personid",
					personid,
					new String[]{"discount"})[0]);
		}
		
		// apply the discount to the item
		total -= (total * ((float)percent / (float)100));
		
		// return the new total
		return total;
	}
	
	/***************************************************
	 ** Action Listeners for the different components **
	 ***************************************************/
	// Selecting items on a list
	public void createActionListeners(LoginScreen ls) {
		
		// switch to the password field when the user presses enter after entering their username
		userField.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				passField.requestFocus();
			}
		});
		
		// submit when pressing enter on the password field
		passField.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				processLogin();
		        pack();
			}
		});
		
		// submit when pressing the login button
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				processLogin();
		        pack();
			}
		});
		
		// reset fields when pressing the clear button
		clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				resetUserLoginInfo();		// clear the user and password fields
				userField.requestFocus();	// move cursor to the username field
				
			}
		});

		// closes application when pressing the cancel button
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ls.dispose();
			}
		});

		// goes back to the login panel when the user clicks logout
		logout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				switchToCard(1);					// go to the login panel
		        incorrectLogin.setVisible(false);	// remove the incorrect login text if present
		        
		        resetUserLoginInfo();				// reset any variables that stored the user's information
		        userField.requestFocus();			// move cursor to the username field
				
		        pack();
			}
		});
		
		order.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String cardnum = cardNumField.getText();
				if(checkCardValidity(personid, cardnum)){
					addOrderToDatabase(cardnum);
					cardNumField.setText("");
				}
				else {
					// display incorrect card message
				}
			}		
		});
		
		// gets string of item clicked in menuList
		menuList.addListSelectionListener(new ListSelectionListener() {
			 
			@Override
	         public void valueChanged(ListSelectionEvent arg0) {
	                if (!arg0.getValueIsAdjusting()) {
	                	itemLabel.setText(menuList.getSelectedValue().toString());
	                	String itemname = itemLabel.getText();
	                	updateItemInfo(itemname);
	                }
	            }
	        });
		
		// gets string of item clicked in foodList
		foodList.addListSelectionListener(new ListSelectionListener() {
			 
			@Override
	         public void valueChanged(ListSelectionEvent arg0) {
	                if (!arg0.getValueIsAdjusting()) {
	                	itemLabel.setText(foodList.getSelectedValue().toString());
	                	String itemname = itemLabel.getText();
	                	updateItemInfo(itemname);
	                }
	            }
	        });
		
		// gets string of item clicked in drinkList
		drinkList.addListSelectionListener(new ListSelectionListener() {
			 
			@Override
	         public void valueChanged(ListSelectionEvent arg0) {
	                if (!arg0.getValueIsAdjusting()) {
	                	itemLabel.setText(drinkList.getSelectedValue().toString());
	                	String itemname = itemLabel.getText();
	                	updateItemInfo(itemname);
	                }
	            }
	        });
			
		}
	}	

