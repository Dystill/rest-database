package OrderTracker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.*;

public class LoginScreen extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane,
	               panel,
	               entrypanel,
	               userPanel,
	               passPanel,
	               buttonPanel;
	
	private final JTextField userField = new JTextField();
	private final JPasswordField passField = new JPasswordField();
	
	private final JLabel userLabel = new JLabel("Username"),
	                     passLabel = new JLabel("Password"),
	                     incorrectLogin = new JLabel("Correct Username and Password! You're an Employee!");

    private final JButton submit = new JButton("Login");
    private final JButton clear = new JButton("Clear");
    private final JButton cancel = new JButton("Exit");
    
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
		contentPane.setLayout(new CardLayout());	// set the main window to a card layout
		
		// create a new panel with a border layout
		panel = new JPanel();
		panel.setLayout(new BorderLayout());

		// create an panel with entry fields
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
		panel.add(entrypanel, BorderLayout.CENTER);
		panel.add(incorrectLogin, BorderLayout.SOUTH);
		contentPane.add(panel);
		
		
		
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
				showIncorrectLoginText();
			}
		});
		
		// submit when pressing the login button
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showIncorrectLoginText();
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
		
		
		
		
		// condense and center the window before displaying
        pack();
        setLocationRelativeTo(null);
        
        // removes the incorrect login text
		incorrectLogin.setVisible(false);
        
	}

	public boolean lookupLogin(String table, String username, String password) {
		SQLQuerier query = new SQLQuerier("Restaurant");
		String prefix = (table.equals("Customers")) ? "c" : "e";
		
		boolean valid = false;
		
		if(query.searchFor(username, prefix + "username", table) &&
				query.searchFor(password, prefix + "password", table))
			valid = true;
		
		return valid;
	}
	
	public void showIncorrectLoginText() {
		if(lookupLogin("Customers", userField.getText(), new String(passField.getPassword()))) {
			// go to menu item list page
			incorrectLogin.setVisible(false);
			incorrectLogin.setForeground(Color.GREEN);
			incorrectLogin.setText("Correct Username and Password!");
			incorrectLogin.setVisible(true);
		}
		else if(lookupLogin("Employees", userField.getText(), new String(passField.getPassword()))) {
			// go to menu item list page
			employee = true;
			incorrectLogin.setVisible(false);
			incorrectLogin.setForeground(Color.GREEN);
			incorrectLogin.setText("Correct Username and Password! You're an Employee!");
			incorrectLogin.setVisible(true);
		}
		else {
			incorrectLogin.setVisible(false);
			incorrectLogin.setForeground(Color.RED);
			incorrectLogin.setText("Incorrect Username or Password.");
			incorrectLogin.setVisible(true);
		}
	}
	
}
