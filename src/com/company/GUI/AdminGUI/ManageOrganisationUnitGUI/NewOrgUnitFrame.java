package com.company.GUI.AdminGUI.ManageOrganisationUnitGUI;

import com.company.Database.OrganisationUnit.OrganisationUnitData;
import com.company.GUI.AdminGUI.ManageUsersGUI.NewPersonGUI;
import com.company.Model.OrganisationUnit;

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
        // Will need to add REGEX CHECK TO SEE IF IT IS A NUMBER
        String name = unitNameField.getText();
        Double credits = Double.parseDouble(creditsField.getText());

        if (name.equals("")) {
            JOptionPane.showMessageDialog(getContentPane(), "Error: Please ensure all fields are valid");

        } else {
            organisationUnitData.addOrganisationUnit(new OrganisationUnit(name, credits));
            JOptionPane.showMessageDialog(getContentPane(), "Successfully added new organisation unit :)");
            NewOrgUnitFrame.this.dispose();
        }

    }

}
