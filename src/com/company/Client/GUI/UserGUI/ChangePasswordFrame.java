package com.company.Client.GUI.UserGUI;

import com.company.Client.Client;
import com.company.Client.Data.UsersData;
import com.company.Testing.TestingException;
import com.company.Common.PasswordHasher;

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
        private String oldPassword;

        public ChangePasswordFrame(UsersData usersData, String oldPassword) {
            super("Change Password");
            setDefaultLookAndFeelDecorated(true);
            setLayout(new BorderLayout());

            this.usersData = usersData;
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
            String hashedPassword = null;
            try {
                hashedPassword = PasswordHasher.hashString(String.valueOf(passwordField.getPassword()));
                String hashedConfirmPassword = PasswordHasher.hashString(String.valueOf(confirmPasswordField.getPassword()));
                //If the new password is the same as it was, then the user is prompted to use a new password
                if (hashedPassword.equals(oldPassword)) {
                    JOptionPane.showMessageDialog(getContentPane(), "Error: Please ensure new password is not the same as old password");
                    passwordField.setText("");
                    confirmPasswordField.setText("");
                }//If the passwords match then
                else if (hashedPassword.equals(hashedConfirmPassword)) {
                    try {
                        usersData.changePassword(hashedPassword, Client.getLoggedInUserID());
                    } catch (TestingException e) {
                        JOptionPane.showMessageDialog(getContentPane(), "Password has been changed");
                    }
                    JOptionPane.showMessageDialog(getContentPane(), "Password has been changed");
                    ChangePasswordFrame.this.dispose();
                } else {
                    JOptionPane.showMessageDialog(getContentPane(), "Error: Please ensure password fields match");
                    passwordField.setText("");
                    confirmPasswordField.setText("");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(getContentPane(), e.getMessage());
            }
        }
}

