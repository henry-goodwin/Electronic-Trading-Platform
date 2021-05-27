package com.company.GUI.AdminGUI.ManageOrganisationUnitGUI;

import com.company.Database.OrganisationUnit.OrganisationUnitData;
import com.company.Database.Persons.PersonsData;
import com.company.Database.Users.UsersData;
import com.company.GUI.AdminGUI.ManageUsersGUI.NewUserGUI;
import com.company.NetworkDataSource.OrganisationUnitNDS;

import javax.swing.*;
import java.awt.*;

public class ManageOrgUnitsFrame extends JFrame {

    private OrganisationUnitData organisationUnitData;

    private JMenuBar menuBar;
    private JMenu newMenu;
    private JMenuItem newOrganisationUnit;

    private JPanel manageOrgPanel;


    public ManageOrgUnitsFrame(OrganisationUnitData organisationUnitData) {
        super("Manage Organisation Unit");
        setDefaultLookAndFeelDecorated(true);
        setLayout(new BorderLayout());
        this.organisationUnitData = organisationUnitData;

        menuBar = new JMenuBar();
        newMenu = new JMenu("New");
        newOrganisationUnit = new JMenuItem("New Organisation Unit");
        newOrganisationUnit.addActionListener(e -> newOrgUnitFrame());
        menuBar.add(newMenu);

        newMenu.add(newOrganisationUnit);

        add(menuBar, BorderLayout.PAGE_START);

        manageOrgPanel = new JPanel();
        add(manageOrgPanel, BorderLayout.CENTER);

        setSize(500,500);
        setLocationByPlatform(true);
        setVisible(true);

    }

    private void newOrgUnitFrame () {
        new NewOrgUnitFrame(new OrganisationUnitData(new OrganisationUnitNDS()));
    }
}
