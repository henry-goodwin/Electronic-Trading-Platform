package com.company.GUI.LoginGUI;

import com.company.Database.DBConnector;
import com.company.Database.OrgUnitAssets.OrgAssetData;
import com.company.Database.Users.UsersData;
import com.company.GUI.AdminGUI.AdminFrame;
import com.company.GUI.TradingGUI.AssetsFrame;
import com.company.Model.User;
import com.company.NetworkDataSource.OrgAssetNDS;
import com.company.Utilities.PasswordHasher;

import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * Frame that logs in users to the trading platform
 * @author Henry Goodwin
 */
public class LoginFrame extends JFrame {

    private JPanel loginPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    UsersData usersData;

    /**
     * Create login frame gui
     */
    public LoginFrame(UsersData usersData) {
        super("Login to Trading Platform");

        this.usersData = usersData;

        setDefaultLookAndFeelDecorated(true);
        setLayout(new BorderLayout());

        addClosingListener(new ClosingListener());

        loginPanel = new JPanel();

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");

        add(loginPanel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> login());
        setupLayout();
        setSize(500,500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setVisible(true);

    }

    /**
     * Login the user
     */
    private void login() {
        String username = usernameField.getText();
        String hashPassword = PasswordHasher.hashString(String.valueOf(passwordField.getPassword()));

        // Reset the password
        passwordField.setText("");

        if (usersData.get(username) != null) {

            User user = usersData.get(username);

            if (user.getPasswordHash().equals(hashPassword)){
                //Login the user

                // Check if user is admin
                if (user.getAccountType().equals("Admin")) {
                    // Present Admin Screen
                    new AdminFrame();
                    LoginFrame.this.dispose();

                } else if (user.getAccountType().equals("Standard")) {
                    // Present Standard Screen
                    Integer orgID = 1;
                    new AssetsFrame(new OrgAssetData(new OrgAssetNDS(), orgID));
                    LoginFrame.this.dispose();
                }
            } else {
                JOptionPane.showMessageDialog(getContentPane(), "Login Failed: Invalid Username/Password");

            }
        } else {
            JOptionPane.showMessageDialog(getContentPane(), "Login Failed: Invalid Username/Password");
        }

        // Login and open GUI
    }

    /**
     * Adds a listener to the JFrame
     */
    private void addClosingListener(WindowListener listener) {
        addWindowListener(listener);
    }

    /**
     * Setup graphical elements and add components to panel
     */
    private void setupLayout() {

        // Setup Grid Bag Layout
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridwidth = 1;


        // Add Username Label
//        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.PAGE_START;
        loginPanel.add(new JLabel("Username: "), constraints);

        // Add Username Field
//        constraints.gridx = 0;
        constraints.gridy = 1;
        loginPanel.add(usernameField, constraints);

        // Add Password Label
        constraints.gridy = 2;
        loginPanel.add(new JLabel("Password: "), constraints);

        // Add Password Field
        constraints.gridy = 3;
        loginPanel.add(passwordField, constraints);

        // Add Login Button
        constraints.gridy = 4;
        loginPanel.add(loginButton, constraints);
    }

    /**
     * Implements the windowClosing method from WindowAdapter/WindowListener to
     * persist the contents of the data/model.
     */
    private class ClosingListener extends WindowAdapter {

        /**
         * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
         */
        public void windowClosing(WindowEvent e) {
            usersData.persist();
            System.exit(0);
        }
    }

}
