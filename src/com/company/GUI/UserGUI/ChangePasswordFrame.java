package com.company.GUI.UserGUI;

import com.company.Client;
import com.company.Database.Persons.PersonsData;
import com.company.Database.Users.UsersData;
import com.company.Model.Person;
import com.company.Model.User;
import com.company.Utilities.PasswordHasher;

import javax.swing.*;
import java.awt.*;

public class ChangePasswordFrame extends JFrame {

/**
 * Frame that  allows users to change their passwords
 * @author Matthew Rowen */
        private UsersData usersData;

        private JPanel ChangePasswordFrame;

        private JPasswordField passwordField;
        private JPasswordField confirmPasswordField;
        private JComboBox accountTypeComboBox;
        private JList identityList;
        private JButton changePasswordButton;
        private String username;
        private String oldPassword;

        public ChangePasswordFrame(UsersData usersData, String username, String oldPassword) {
            super("Change Password");
            setDefaultLookAndFeelDecorated(true);
            setLayout(new BorderLayout());

            this.usersData = usersData;
            this.username = username;
            this.oldPassword = oldPassword;

            passwordField = new JPasswordField();
            confirmPasswordField = new JPasswordField();
            changePasswordButton = new JButton("Change Password");

            ChangePasswordFrame = new JPanel();
            add(ChangePasswordFrame, BorderLayout.CENTER);

            setupLayout();

            changePasswordButton.addActionListener(e -> changePassword());

            setSize(500, 500);
//        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            setLocationByPlatform(true);
            setVisible(true);
        }

        private void setupLayout() {
            ChangePasswordFrame.setLayout(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();

            constraints.gridwidth = 1;
            constraints.fill = GridBagConstraints.BOTH;
            constraints.anchor = GridBagConstraints.PAGE_START;

            constraints.gridy = 2;
            ChangePasswordFrame.add(new JLabel("Password"), constraints);

            constraints.gridy = 3;
            ChangePasswordFrame.add(passwordField, constraints);

            constraints.gridy = 4;
            ChangePasswordFrame.add(new JLabel("Confirm Password"), constraints);

            constraints.gridy = 5;
            ChangePasswordFrame.add(confirmPasswordField, constraints);


            constraints.gridy = 10;
            ChangePasswordFrame.add(changePasswordButton, constraints);
        }

        private void changePassword() {
            // Check status of fields
                // Perform Checks on passwords
                String hashedPassword = PasswordHasher.hashString(String.valueOf(passwordField.getPassword()));
                String hashedConfirmPassword = PasswordHasher.hashString(String.valueOf(confirmPasswordField.getPassword()));
                //If the new password is the same as it wasz, then the user is prompted to use a new passsword
                if (hashedPassword.equals(oldPassword)) {
                    JOptionPane.showMessageDialog(getContentPane(), "Error: Please ensure new password is not the same as old password");
                    passwordField.setText("");
                    confirmPasswordField.setText("");
                }//If the passwords match then
                else if (hashedPassword.equals(hashedConfirmPassword)) {
                    usersData.changePassword(hashedPassword, Client.getLoggedInUserID());
                    JOptionPane.showMessageDialog(getContentPane(), "Password has been changed");
                    ChangePasswordFrame.this.dispose();
                } else {
                    JOptionPane.showMessageDialog(getContentPane(), "Error: Please ensure password fields match");
                    passwordField.setText("");
                    confirmPasswordField.setText("");
                }
            }
        }

