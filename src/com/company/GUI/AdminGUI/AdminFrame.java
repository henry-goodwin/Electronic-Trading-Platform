package com.company.GUI.AdminGUI;

import com.company.Database.OrganisationUnit.OrganisationUnitData;
import com.company.Database.Users.UsersData;
import com.company.GUI.AdminGUI.ManageOrganisationUnitGUI.ManageOrgUnitsFrame;
import com.company.GUI.AdminGUI.ManageUsersGUI.ManageUsersFrame;
import com.company.GUI.LoginGUI.LoginFrame;

import javax.swing.*;
import java.awt.*;

public class AdminFrame extends JFrame {

    private JPanel adminPanel;
    private JButton manageOrganisationalUnitsButton;
    private JButton manageUsersButton;

    public AdminFrame() {

        super("Admin Control Panel");

        setDefaultLookAndFeelDecorated(true);
        setLayout(new BorderLayout());

        adminPanel = new JPanel();
        manageOrganisationalUnitsButton = new JButton("Manage Organisational Units");
        manageUsersButton = new JButton("Manage Users");
        add(adminPanel, BorderLayout.CENTER);

        manageOrganisationalUnitsButton.addActionListener(e -> manageOrg());
        manageUsersButton.addActionListener(e -> manageUsers());
        setupLayout();
        setSize(500,500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setVisible(true);
    }

    private void setupLayout() {

        adminPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridwidth = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.PAGE_START;

        constraints.gridy = 0;
        constraints.gridx = 0;
        adminPanel.add(manageOrganisationalUnitsButton, constraints);

        constraints.gridy = 1;
        constraints.gridx = 0;
        adminPanel.add(manageUsersButton, constraints);

    }

    private void manageOrg() {
        new ManageOrgUnitsFrame(new OrganisationUnitData());
    }

    private void manageUsers() {

        new ManageUsersFrame(new UsersData());

    }

}
