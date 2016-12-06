package OrderTracker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.border.*;

public class LoginScreen extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane,
	               loginpanel,
	               menuPanel,
	               greetingPanel,
	               itemsPanel,
	               entrypanel,
	               userPanel,
	               passPanel,
	               buttonPanel;
	
	private final JTextField userField = new JTextField();
	private final JPasswordField passField = new JPasswordField();
	
	private final JLabel userLabel = new JLabel("Username"),
	                     passLabel = new JLabel("Password"),
	                     greetingLabel = new JLabel("Hello", SwingConstants.CENTER),
	                     testLabel = new JLabel("TEST"),
	                     incorrectLogin = new JLabel("Correct Username and Password! You're an Employee!");

    private final JButton submit = new JButton("Login");
    private final JButton clear = new JButton("Clear");
    private final JButton cancel = new JButton("Exit");
    private final JButton logout = new JButton("Logout");
    
    private CardLayout cl = new CardLayout();
    
    private int borderSize = 20;
    private boolean employee = true;

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
		
		createLoginPanel();
		createMenuPanel();

		contentPane.add(loginpanel, "1");
		contentPane.add(menuPanel, "2");
		
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
			}
		});
		
		// condense and center the window before displaying
        pack();
        setLocationRelativeTo(null);
        
        // removes the incorrect login text
		incorrectLogin.setVisible(false);
        
	}
	
	private void createMenuPanel() {
		// menu panel - greeting
		menuPanel = new JPanel();

		menuPanel.setLayout(new BorderLayout());
		
		// greeting panel
		greetingPanel = new JPanel();
		greetingPanel.setLayout(new BorderLayout());
		
		greetingPanel.add(greetingLabel, BorderLayout.NORTH);
		
		// food and drink items tabs
		JTabbedPane itemsPanel = new JTabbedPane();
	    
		// food
		JComponent foodPanel = makeTextPanel("Food");
	    itemsPanel.addTab("Food Items", foodPanel);
	    itemsPanel.setMnemonicAt(0, KeyEvent.VK_1);
	    
	    //drink
	    JComponent drinkPanel = makeTextPanel("Drink");
	    itemsPanel.addTab("Drink Items", drinkPanel);
	    itemsPanel.setMnemonicAt(1, KeyEvent.VK_2);
	        
		// add two (greeting and food/drink panels) to main menu panel
		menuPanel.add(greetingPanel, BorderLayout.NORTH);
		menuPanel.add(itemsPanel, BorderLayout.CENTER);
		menuPanel.add(logout, BorderLayout.SOUTH);

		//menuPanel.add(menuLabel);
		//menuPanel.add(logout);

	}
	
	protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
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

	public boolean lookupLogin(String table, String username, String password) {
		SQLQuerier query = new SQLQuerier("Restaurant");
		String prefix = (table.equals("Customers")) ? "c" : "e";
		
		boolean valid = false;
		
		String loginInfo[] = {username, password};
		String columns[] = {prefix + "username", prefix + "password"};
		
		/*
		if(query.searchForTwoCols(table, prefix + "username", username,
				                         prefix + "password", password)) {
			valid = true;
		}
		/**/
		
		if(query.searchFor(table, columns, loginInfo)) {
			valid = true;
		}
		
		return valid;
	}
	
	public void processLogin() {
		if(lookupLogin("Customers", userField.getText(), new String(passField.getPassword()))) {
			// go to menu item list page
			cl.show(contentPane, "2");
		}
		else if(lookupLogin("Employees", userField.getText(), new String(passField.getPassword()))) {
			// go to menu item list page
			employee = true;
			cl.show(contentPane, "2");
		}
		else {
			incorrectLogin.setVisible(false);
			incorrectLogin.setForeground(Color.RED);
			incorrectLogin.setText("Incorrect Username or Password.");
			incorrectLogin.setVisible(true);
		}
	}
	
}
