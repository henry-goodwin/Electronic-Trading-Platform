package com.company.Client.GUI.AdminGUI.ManageOrganisationUnitGUI;

import com.company.Client.Data.OrganisationUnitData;
import com.company.Common.Model.OrganisationUnit;

import javax.swing.*;
import java.awt.*;

public class NewOrgUnitFrame extends JFrame {

    private OrganisationUnitData organisationUnitData;

    private JPanel newOrgPanel;

    private JTextField unitNameField;
    private JTextField creditsField;
    private JButton addNewUnitButton;

    public NewOrgUnitFrame(OrganisationUnitData organisationUnitData) {
        super("Add New Organisation Unit");
        setDefaultLookAndFeelDecorated(true);
        setLayout(new BorderLayout());

        this.organisationUnitData = organisationUnitData;
        unitNameField = new JTextField();
        creditsField = new JTextField();
        addNewUnitButton = new JButton("Add Organisation Unit");
        addNewUnitButton.addActionListener(e -> addNewUnit());

        newOrgPanel = new JPanel();
        add(newOrgPanel, BorderLayout.CENTER);

        setupLayout();
        setSize(500,500);
        setLocationByPlatform(true);
        setVisible(true);

    }

    private void setupLayout() {
        newOrgPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;

        constraints.gridy = 0;
        newOrgPanel.add(new JLabel("Name"), constraints);

        constraints.gridy = 1;
        newOrgPanel.add(unitNameField, constraints);

        constraints.gridy = 2;
        newOrgPanel.add(new JLabel("Credits"), constraints);

        constraints.gridy = 3;
        newOrgPanel.add(creditsField, constraints);

        constraints.gridy = 4;
        newOrgPanel.add(addNewUnitButton, constraints);

    }

    private void addNewUnit() {
        //get the entered data
        String name = unitNameField.getText();
        String rawCredits = creditsField.getText();
        //regex to check only numeric characters
        if(!rawCredits.matches("[0-9]+")){
            JOptionPane.showMessageDialog(getContentPane(), "Error: Please enter a valid number");
        } //check to see if name valid
        else if (name.equals("")) {
            JOptionPane.showMessageDialog(getContentPane(), "Error: Please ensure all fields are valid");

        } else {
            //both are valid so enter data
            Double credits = Double.parseDouble(rawCredits);
            try {
                OrganisationUnit organisationUnit = new OrganisationUnit();
                organisationUnit.setName(name);
                organisationUnit.setCredits(credits);

                organisationUnitData.addOrganisationUnit(organisationUnit);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(getContentPane(), "Error: Please ensure credits are greater than 0");
            }
            JOptionPane.showMessageDialog(getContentPane(), "Successfully added new organisation unit :)");
            NewOrgUnitFrame.this.dispose();
        }

    }

}
