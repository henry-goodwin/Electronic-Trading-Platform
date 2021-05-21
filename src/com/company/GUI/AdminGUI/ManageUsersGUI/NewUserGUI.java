package com.company.GUI.AdminGUI.ManageUsersGUI;

import com.company.Database.Persons.PersonsData;
import com.company.Database.Users.UsersData;
import com.company.Model.Person;
import com.company.Model.User;
import com.company.Utilities.PasswordHasher;

import javax.swing.*;
import java.awt.*;

public class NewUserGUI extends JFrame {

    private UsersData usersData;
    private PersonsData personsData;

    private JPanel newUserPanel;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox accountTypeComboBox;
    private JList identityList;
    private JButton addNewUserButton;

    private String[] accountTypes = { "Standard", "Admin"};

    public NewUserGUI(UsersData usersData, PersonsData personsData) {
        super("Add New User");
        setDefaultLookAndFeelDecorated(true);
        setLayout(new BorderLayout());

        this.usersData = usersData;
        this.personsData = personsData;

        usernameField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();
        accountTypeComboBox = new JComboBox(accountTypes);
        accountTypeComboBox.setSelectedIndex(0);
        addNewUserButton = new JButton("Add new user");

        identityList = new JList(personsData.getPersonsModel());

        newUserPanel = new JPanel();
        add(newUserPanel, BorderLayout.CENTER);

        setupLayout();

        addNewUserButton.addActionListener(e -> addNewUser());

        setSize(500,500);
//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setVisible(true);
    }

    private void setupLayout() {
        newUserPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;

        constraints.gridy = 0;
        newUserPanel.add(new JLabel("Username"), constraints);

        constraints.gridy = 1;
        newUserPanel.add(usernameField, constraints);

        constraints.gridy = 2;
        newUserPanel.add(new JLabel("Password"), constraints);

        constraints.gridy = 3;
        newUserPanel.add(passwordField, constraints);

        constraints.gridy = 4;
        newUserPanel.add(new JLabel("Confirm Password"), constraints);

        constraints.gridy = 5;
        newUserPanel.add(confirmPasswordField, constraints);

        constraints.gridy = 6;
        newUserPanel.add(new JLabel("Account Type"), constraints);

        constraints.gridy = 7;
        newUserPanel.add(accountTypeComboBox, constraints);

        constraints.gridy = 8;
        newUserPanel.add(new JLabel("Select users identity"), constraints);

        constraints.gridy = 9;
        newUserPanel.add(identityList, constraints);

        constraints.gridy = 10;
        newUserPanel.add(addNewUserButton, constraints);
    }

    private void addNewUser() {
        // Check status of fields
        if (usernameField.getText().equals("") ||
                passwordField.getPassword().length == 0  ||
                confirmPasswordField.getPassword().length == 0 || identityList.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(getContentPane(), "Error: Please ensure all fields are valid");
        } else {
            //


            // Perform Checks on passwords
            String hashedPassword = PasswordHasher.hashString(String.valueOf(passwordField.getPassword()));
            String hashedConfirmPassword = PasswordHasher.hashString(String.valueOf(confirmPasswordField.getPassword()));

            if (hashedPassword.equals(hashedConfirmPassword)) {

                String username = usernameField.getText();

                // Check that username is free
                if (usersData.checkUsernameAvailability(username)) {
                    // Username is available, add user

                    Person person = (Person) identityList.getSelectedValue();
                    User user = new User((String) accountTypeComboBox.getSelectedItem(), person, username, hashedPassword);
                    usersData.addUser(user);
                    JOptionPane.showMessageDialog(getContentPane(), "Successfully added new user :)");
                    NewUserGUI.this.dispose();


                } else {
                    JOptionPane.showMessageDialog(getContentPane(), "Error: Username is taken, please try another name");
                }

            } else {
                JOptionPane.showMessageDialog(getContentPane(), "Error: Please ensure password fields match");
            }
        }
    }

}
