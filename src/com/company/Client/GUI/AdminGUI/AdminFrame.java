package com.company.Client.GUI.AdminGUI;

import com.company.Client.Client;
import com.company.Client.Data.AssetData;
import com.company.Client.Data.OrganisationUnitData;
import com.company.Client.Data.UsersData;
import com.company.Client.GUI.AdminGUI.ManageOrganisationUnitGUI.ManageOrgUnitsFrame;
import com.company.Client.GUI.AdminGUI.ManageUsersGUI.ManageUsersFrame;
import com.company.Client.GUI.LoginGUI.LoginFrame;
import com.company.Common.NetworkDataSource.AssetNDS;
import com.company.Common.NetworkDataSource.OrganisationUnitNDS;
import com.company.Common.NetworkDataSource.UsersNDS;
import com.company.Testing.TestingException;

import javax.swing.*;
import java.awt.*;

public class AdminFrame extends JFrame {

    private JPanel adminPanel;
    private JButton manageOrganisationalUnitsButton;
    private JButton manageUsersButton;
    private JButton manageAssetsButton;

    private JMenuBar adminMenuBar;
    private JMenu userMenu;
    private JMenuItem logoutButton;

    public AdminFrame() {

        super("Admin Control Panel");

        setDefaultLookAndFeelDecorated(true);
        setLayout(new BorderLayout());

        adminMenuBar = new JMenuBar();
        userMenu = new JMenu("Account");
        logoutButton = new JMenuItem("Logout");
        logoutButton.addActionListener(e -> logout());
        adminMenuBar.add(userMenu);
        userMenu.add(logoutButton);

        add(adminMenuBar, BorderLayout.PAGE_START);

        adminPanel = new JPanel();
        manageOrganisationalUnitsButton = new JButton("Manage Organisational Units");
        manageUsersButton = new JButton("Manage Users");
        manageAssetsButton = new JButton("Manage Assets");
        add(adminPanel, BorderLayout.CENTER);

        manageOrganisationalUnitsButton.addActionListener(e -> manageOrg());
        manageUsersButton.addActionListener(e -> manageUsers());
        manageAssetsButton.addActionListener(e -> manageAssets());
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

        constraints.gridy = 2;
        constraints.gridx = 0;
        adminPanel.add(manageAssetsButton, constraints);

    }

    private void logout() {
        Client.logout();

        for(Frame frame: getFrames()) {
            frame.dispose();
        }

        try {
            new LoginFrame(new UsersData(new UsersNDS()));
        } catch (TestingException e) {
            e.printStackTrace();
        }
    }

    private void manageOrg() {
        new ManageOrgUnitsFrame(new OrganisationUnitData(new OrganisationUnitNDS()));
    }

    private void manageUsers() {

        try {
            new ManageUsersFrame(new UsersData(new UsersNDS()));
        } catch (TestingException e) {
            e.printStackTrace();
        }

    }

    private void manageAssets() {
        try {
            new ManageAssetsFrame(new AssetData(new AssetNDS()));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(getContentPane(), e.getMessage());
        }
    }

}
