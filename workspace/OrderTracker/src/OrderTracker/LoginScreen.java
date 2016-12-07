package OrderTracker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.border.*;

public class LoginScreen extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane,		// the main panel to contain every interface
	               loginpanel,		// for the login window
	               menuPanel,		// for the menu window
	               greetingPanel,	// for the greeting bar in the menu window
	               allMenuTab,	//	both food and drink items tab
	               foodTab,		// for the food items tab
	               drinkTab,		// for the drink items tab
	               entrypanel,		// to hold the text fields in the login window
	               userPanel,		// to hold the username entry field in login
	               passPanel,		// to hold the password entry field in login
	               buttonPanel;		// to hold the buttons in the login
	
	private final JTextField userField = new JTextField();
	private final JPasswordField passField = new JPasswordField();
	
	private final JLabel userLabel = new JLabel("Username"),
	                     passLabel = new JLabel("Password"),
	                     greetingLabel = new JLabel("Hello", SwingConstants.CENTER),
	                     incorrectLogin = new JLabel("");

    private final JButton submit = new JButton("Login");
    private final JButton clear = new JButton("Clear");
    private final JButton cancel = new JButton("Exit");
    private final JButton logout = new JButton("Logout");
    
    private CardLayout cl = new CardLayout();
    
    private int borderSize = 20;
    private boolean isEmployee = true;

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
	public LoginScreen() {
		createLoginWindow(this);
	}
	
	public void createLoginWindow(LoginScreen ls) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		// create the main window
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(borderSize, borderSize, borderSize, borderSize));
		setContentPane(contentPane);
        setTitle("Login");
		contentPane.setLayout(cl);	// set the main window to a card layout
		
		// create each main interface of the program
		createLoginPanel();	// login screen
		createMenuPanel();	// menu screen

		// add each interface to the primary content pane
		contentPane.add(loginpanel, "1");
		contentPane.add(menuPanel, "2");
		
		// show the login screen first
		cl.show(contentPane, "1");
		
		/***************************************************
		 ** Action Listeners for the different components **
		 ***************************************************/
		
		// switch to the password field when the user presses enter
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
				passField.setText("");
				userField.setText("");
				userField.requestFocus();
			}
		});

		// closes application when pressing the cancel button
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ls.dispose();
			}
		});

		// closes application when pressing the cancel button
		logout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cl.show(contentPane, "1");
				clear.doClick();
		        pack();
			}
		});
		
		// condense and center the window before displaying
        pack();
        setLocationRelativeTo(null);
        
        // removes the incorrect login text
		incorrectLogin.setVisible(false);
        
	}
	
	private void createMenuPanel() {
		
		String greeting = "Hello " + (isEmployee ? "Employee!" : "Customer!");
		
		// menu panel - greeting
		menuPanel = new JPanel();
		menuPanel.setLayout(new BorderLayout());
		
		// greeting panel
		greetingPanel = new JPanel();
		greetingPanel.setLayout(new BorderLayout());
		greetingPanel.setBorder(new EmptyBorder(0, 0, borderSize, 0));
		
		greetingLabel.setText(greeting);
		greetingPanel.add(greetingLabel, BorderLayout.CENTER);
		greetingPanel.add(logout, BorderLayout.EAST);
		
		// food and drink items multiple tabs
		JTabbedPane itemsPanel = new JTabbedPane();
	    
		// all menu items tab
		allMenuTab = new JPanel();
		allMenuTab.setLayout(new BorderLayout());
		itemsPanel.addTab("All Items", allMenuTab);
		
		// TEST printing to all menu tab
		/**DefaultListModel model = new DefaultListModel();
		JList list = new JList(model);
		    
		for (int i = 1; i <10; i++)
		    model.addElement("Food " + i);
		    
		 allMenuTab.add(list);**/
	
		// food items tab
		foodTab = new JPanel();
		foodTab.setLayout(new BorderLayout());
	    itemsPanel.addTab("Food Items", foodTab);
	    
	    // print food items
	  
	  
	    // drink items tab
	    drinkTab = new JPanel();
	    drinkTab.setLayout(new BorderLayout());
	    itemsPanel.addTab("Drink Items", drinkTab);
	
	    // print drink items
	    
	    
		// add (greeting and food/drink panels) to main menu panel
		menuPanel.add(greetingPanel, BorderLayout.NORTH);
		menuPanel.add(itemsPanel, BorderLayout.CENTER);
	}
	

	public void createLoginPanel() {
		// create a new panel with a border layout
		loginpanel = new JPanel();
		loginpanel.setLayout(new BorderLayout());
		
		// create a panel with entry fields
		entrypanel = new JPanel();
		entrypanel.setLayout(new BoxLayout(entrypanel, BoxLayout.PAGE_AXIS));

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
		
		// add the username, password, and button panels to the entrypanel
		entrypanel.add(userPanel);
		entrypanel.add(passPanel);
		entrypanel.add(buttonPanel);
		
		// add the entry panel to the center of the main border layout panel
		loginpanel.add(entrypanel, BorderLayout.CENTER);
		loginpanel.add(incorrectLogin, BorderLayout.SOUTH);
	}
	
	// print string array to a JLIST
	public void printMenu(String table, String column, String[] list)
	{
		//call getListOf function
		SQLQuerier query = new SQLQuerier("Menu");
		String[] menu = query.getListOf(table, column);
		
		DefaultListModel model = new DefaultListModel();
		for (int i=0, n = list.length; i<n; i++)
			model.addElement(list[i]);

		JList menuList = new JList(model);
		
		allMenuTab.add(menuList);
	}

	public boolean lookupLogin(String table, String username, String password) {
		// create a SQLQuerier object to handle queries
		SQLQuerier query = new SQLQuerier("Restaurant");
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
		// check the customers table for the login info
		if(lookupLogin("Customers", userField.getText(), new String(passField.getPassword()))) {
			// go to menu item list page
			isEmployee = false;
			cl.show(contentPane, "2");
		}
		// check the employees table for the login info
		else if(lookupLogin("Employees", userField.getText(), new String(passField.getPassword()))) {
			// go to menu item list page
			isEmployee = true;
			cl.show(contentPane, "2");
		}
		// show text saying the login info was incorrect
		else {
			incorrectLogin.setVisible(false);
			incorrectLogin.setForeground(Color.RED);
			incorrectLogin.setText("Incorrect Username or Password.");
			incorrectLogin.setVisible(true);
		}
	}
	
}
