package com.company.GUI.LoginGUI;

import com.company.Client;
import com.company.Database.OrgUnitAssets.OrgAssetData;
import com.company.Database.OrganisationUnit.OrganisationUnitData;
import com.company.Database.Persons.PersonsData;
import com.company.Database.Users.UsersData;
import com.company.GUI.AdminGUI.AdminFrame;
import com.company.GUI.TradingGUI.AssetsFrame;
import com.company.Model.User;
import com.company.NetworkDataSource.OrgAssetNDS;
import com.company.NetworkDataSource.OrganisationUnitNDS;
import com.company.NetworkDataSource.PersonsNDS;
import com.company.Server.Command;
import com.company.Utilities.PasswordHasher;

import javax.swing.JOptionPane;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Frame that logs in users to the trading platform
 * @author Henry Goodwin
 */
public class LoginFrame extends JFrame {

    private JPanel loginPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    private UsersData usersData;

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

        if(usersData.login(username, hashPassword)) {
            User loggedInUser = usersData.get(username);
            Client.setUserID(loggedInUser.getUserID());
            Client.setPersonID(loggedInUser.getPersonID());
            // Check if user is admin
            if (loggedInUser.getAccountType().equals("Admin")) {
                // Present Admin Screen
                new AdminFrame();

            } else if (loggedInUser.getAccountType().equals("Standard")) {
                // Present Standard Screen
                Client.setOrgID(1);
                // Need to find out what orgID user belongs to
                new AssetsFrame(new OrgAssetData(new OrgAssetNDS()), new OrganisationUnitData(new OrganisationUnitNDS()), new PersonsData(new PersonsNDS()), usersData, username);
            }

            LoginFrame.this.dispose();

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
