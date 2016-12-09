package OrderTracker;

import java.awt.*;
import java.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JList;
import javax.swing.event.*;

import javax.swing.*;
import javax.swing.border.*;

public class LoginScreen extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane,		// the main panel to contain every interface
	               loginPanel,		// for the login window
	               entryPanel,		// to hold the text fields in the login window
	               textPanel,		// to hold the user and pass fields
	               userPanel,		// to hold the username entry field in login
	               passPanel,		// to hold the password entry field in login
	               buttonPanel,		// to hold the buttons in the login
	               menuPanel,		// for the menu window
	               greetingPanel,	// for the greeting bar in the menu window
	               allMenuTab,		//	both food and drink items tab
	               foodTab,			// for the food items tab
	               drinkTab,		// for the drink items tab
	               infoPanel,		// to hold the item info to the right
	               orderPanel,
	               orderInfoPanel,
	               cardEntryPanel;
	
	private JList menuList,
			      foodList,
			      drinkList;
	
	private final JScrollPane menuScroll = new JScrollPane(), 
						      foodScroll = new JScrollPane(),
						      drinkScroll = new JScrollPane();
	
	private final JTextField userField = new JTextField();
	private final JPasswordField passField = new JPasswordField();
	
	private final JLabel userLabel = new JLabel("Username"),
	                     passLabel = new JLabel("Password"),
	                     greetingLabel = new JLabel("Hello", SwingConstants.CENTER),
	                     incorrectLogin = new JLabel(""),
	    	             welcomeLabel = new JLabel(
	    	            		 "Welcome to the Restaurant Order Tracking System!",
	    	            		 SwingConstants.CENTER);

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
		contentPane = new JPanel();
		contentPane.setBorder(
				new EmptyBorder(borderSize, borderSize, borderSize, borderSize));
		setContentPane(contentPane);
		contentPane.setLayout(cl);	// set the main window to a card layout
		
		// create each main interface of the program
		createLoginPanel();	// login screen
		createMenuPanel();	// menu screen
		createOrderPanel();

		// add each interface to the primary content pane
		contentPane.add(loginPanel, "1");
		contentPane.add(menuPanel, "2");
		contentPane.add(orderPanel, "3");
		
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
		loginPanel = new JPanel();
		loginPanel.setLayout(new BorderLayout());
		
		// create a panel with entry fields
		entryPanel = new JPanel();
		entryPanel.setLayout(new FlowLayout());
		
		textPanel = new JPanel();
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

		// create a panel for the username
		userPanel = new JPanel(new FlowLayout());
		
		// align and size the user textfield
		userField.setHorizontalAlignment(SwingConstants.LEFT);
		userField.setColumns(15);
		
		// add label and textfield to the username panel
		userPanel.add(userLabel);
		userPanel.add(userField);

		// create a panel for the password
		passPanel = new JPanel(new FlowLayout());
		
		// align and size the password field
		passField.setHorizontalAlignment(SwingConstants.LEFT);
		passField.setColumns(15);
			
		// add label and passwordfield to the password panel
		passPanel.add(passLabel);
		passPanel.add(passField);
		
		// create and add a button panel with login and cancel
		buttonPanel = new JPanel(new FlowLayout());	
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
			
			switchToCard(2);					// go to menu screen
			
	        pack();
		}
		// check the employees table for the login info
		else if(lookupLogin("Employees", userField.getText(), new String(passField.getPassword()))) {
			
			isEmployee = true;
			
			currentUser = userField.getText();	// store the username of this user
			
			getUserInfo("Employees", currentUser);
			greetingLabel.setText(formatGreeting("Employee"));
			
			switchToCard(2);					// go to menu screen
			
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
	
	private String formatGreeting(String type) {
		String greeting;

		greeting = "Hello " + fname.substring(0, 1).toUpperCase()
				+ fname.substring(1).toLowerCase() + "! " +
				"(" + type + " #" + personid + ")";
		
		return greeting;
	}
	
	// retrieve and save the current user's information
	private void getUserInfo(String table, String user) {
		
		String[] pidfnln = query.getRowItems(
				"People natural join " + table,
				((table.equals("Customers")) ? "c" : "e") + "username",
				user, new String[]{"personid", "fname", "lname"});

		personid = pidfnln[0];
		fname = pidfnln[1];
		lname = pidfnln[2];
		
	}
	
	// reset any fields and labels that use the user's info.
	private void resetUserLoginInfo() {
		currentUser = fname = lname = personid = "";
		greetingLabel.setText("");
		userField.setText("");
		passField.setText("");
	}
	
	
	/*****************************************
	 ** Methods for creating the Menu Panel **
	 *****************************************/
	
	private void createMenuPanel() {
		
		// menu panel - greeting
		menuPanel = new JPanel();
		menuPanel.setLayout(new BorderLayout());
		
		// greeting panel
		greetingPanel = new JPanel();
		greetingPanel.setLayout(new BorderLayout());
		greetingPanel.setBorder(new EmptyBorder(0, 0, borderSize, 0));
		
		greetingPanel.add(greetingLabel, BorderLayout.CENTER);
		greetingPanel.add(logout, BorderLayout.EAST);
	    
		
		//// Creating the tabs

		// food and drink items multiple tabs
		JTabbedPane itemsPanel = new JTabbedPane();
		
		// all menu items tab
		allMenuTab = new JPanel();
		allMenuTab.setLayout(new BorderLayout());
		itemsPanel.addTab(tabNames[0], allMenuTab);
	
		// food items tab
		foodTab = new JPanel();
		foodTab.setLayout(new BorderLayout());
	    itemsPanel.addTab(tabNames[1], foodTab);
	  	  
	    // drink items tab
	    drinkTab = new JPanel();
	    drinkTab.setLayout(new BorderLayout());
	    itemsPanel.addTab(tabNames[2], drinkTab);
		
	    //// Adding item lists to each tab
	    
		// add all items to the all menu tab
		menuList = populateJList(query.getListOf("MenuItems", "itemname"));
		menuScroll.setViewportView(menuList);
		allMenuTab.add(menuScroll, BorderLayout.WEST);
	    
	    // add all food items to the food tab
	    foodList = populateJList(query.getListOf("FoodItems", "itemname"));
		foodScroll.setViewportView(foodList);
		foodTab.add(foodScroll, BorderLayout.WEST);
	
	    // add all drink items to the drink tab
	    drinkList = populateJList(query.getListOf("DrinkItems", "itemname"));
		drinkScroll.setViewportView(drinkList);
		drinkTab.add(drinkScroll, BorderLayout.WEST);
		
		
		//// Adding the right side info panel
		infoPanel = new JPanel();
		menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		foodList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		drinkList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		//// Adding all panels to the main window
		
		// add (greeting and food/drink panels) to main menu panel
		menuPanel.add(greetingPanel, BorderLayout.NORTH);
		menuPanel.add(itemsPanel, BorderLayout.CENTER);
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
		
		
		
	}

	
	/*********************************************************
	 ** Methods for creating and processing the Order Panel **
	 *********************************************************/
	
	public void createOrderPanel() {
		orderPanel = new JPanel();
		orderPanel.setLayout(new BorderLayout());
		
		
		
	}
	
	
	/***************************************************
	 ** Action Listeners for the different components **
	 ***************************************************/
	// Selecting items on a list
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting())
		{
			ListSelectionModel lsmMenu = (ListSelectionModel)e.getSource();
			
			// indicate which item on the list is clicked
			int firstIndex = e.getFirstIndex();
	        int lastIndex = e.getLastIndex();
		}
	}

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
				switchToCard(3);
			}
		});
		
	}	
}
