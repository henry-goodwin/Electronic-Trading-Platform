package com.company.GUI.AdminGUI.ManageUsersGUI;

import com.company.Database.Persons.PersonsData;
import com.company.Model.Person;

import javax.swing.*;
import java.awt.*;

public class NewPersonGUI extends JFrame {

    private PersonsData personsData;

    private JPanel newPersonPanel;

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JButton addNewPersonButton;

    public NewPersonGUI(PersonsData personsData) {
        super("Add New Person");
        setDefaultLookAndFeelDecorated(true);
        setLayout(new BorderLayout());

        this.personsData = personsData;

        firstNameField = new JTextField();
        lastNameField = new JTextField();
        addNewPersonButton = new JButton("Add new person");
        addNewPersonButton.addActionListener(e -> addNewPerson());

        newPersonPanel = new JPanel();
        add(newPersonPanel, BorderLayout.CENTER);

        setupLayout();

        setSize(500,500);
        setLocationByPlatform(true);
        setVisible(true);
    }

    private void setupLayout() {
        newPersonPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;

        constraints.gridy = 0;
        newPersonPanel.add(new JLabel("Firstname"), constraints);

        constraints.gridy = 1;
        newPersonPanel.add(firstNameField, constraints);

        constraints.gridy = 2;
        newPersonPanel.add(new JLabel("Lastname"), constraints);

        constraints.gridy = 3;
        newPersonPanel.add(lastNameField, constraints);

        constraints.gridy = 4;
        newPersonPanel.add(addNewPersonButton, constraints);
    }

    private void addNewPerson() {
        // Check if fields are empty

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        //check if names have numbers of 
        if (firstName.equals("") || lastName.equals("")) {
            JOptionPane.showMessageDialog(getContentPane(), "Error: Please ensure all fields are valid");
        }else{
            try {
                personsData.addUser(new Person(firstName, lastName));
                JOptionPane.showMessageDialog(getContentPane(), "Successfully added new person :)");
                NewPersonGUI.this.dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(getContentPane(), e.getMessage());
            }
        }
    }

}
