package com.company.GUI.AdminGUI.ManageOrganisationUnitGUI;

import com.company.Database.OrgUnitEmployees.OrgUnitEmployeesData;
import com.company.Database.OrganisationUnit.OrganisationUnitData;
import com.company.Database.Users.UsersData;
import com.company.GUI.AdminGUI.OrgUnitTableModel;
import com.company.GUI.TradingGUI.OrgAssetTableModel;
import com.company.Model.OrganisationUnit;
import com.company.NetworkDataSource.OrganisationUnitNDS;
import com.company.NetworkDataSource.UnitEmployeeNDS;
import com.company.NetworkDataSource.UsersNDS;

import javax.swing.*;
import java.awt.*;

public class ManageOrgUnitsFrame extends JFrame {

    private OrganisationUnitData organisationUnitData;

    private JMenuBar menuBar;
    private JMenu newMenu;
    private JMenuItem newOrganisationUnit;

    private JPanel manageOrgPanel;

    private JTable orgUnitsTable;
    private OrgUnitTableModel orgUnitTableModel;

    private JButton editCreditsBtn;
    private JButton addUsersBtn;

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

        orgUnitTableModel = new OrgUnitTableModel();
        orgUnitTableModel.setData(this.organisationUnitData.getList());

        orgUnitsTable = new JTable(orgUnitTableModel);
        orgUnitsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        editCreditsBtn = new JButton("Edit Credits");
        editCreditsBtn.addActionListener(e -> editCredits());

        addUsersBtn = new JButton("Add User");
        addUsersBtn.addActionListener(e -> addUser());

        manageOrgPanel = new JPanel();
        add(manageOrgPanel, BorderLayout.CENTER);

        setupLayout();

        setSize(500,500);
        setLocationByPlatform(true);
        setVisible(true);

    }

    private void setupLayout() {

        manageOrgPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.insets = new Insets(10,10,10,10);

        constraints.gridy = 0;
        constraints.weighty = 1;
        constraints.weightx = 1;
        manageOrgPanel.add(new JScrollPane(orgUnitsTable), constraints);

        constraints.gridy = 1;
        constraints.weighty = 1;
        constraints.weightx = 1;
        manageOrgPanel.add(editCreditsBtn, constraints);

        constraints.gridy = 2;
        constraints.weighty = 1;
        constraints.weightx = 1;
        manageOrgPanel.add(addUsersBtn, constraints);
    }

    private void editCredits() {

        Double credits = Double.valueOf(JOptionPane.showInputDialog(getContentPane(),"Enter New Credit Amount"));

        if (credits >= 0) {
            OrganisationUnit organisationUnit = (OrganisationUnit) orgUnitTableModel.getValueAt(orgUnitsTable.getSelectedRow(), 0);
            organisationUnit.setCredits(credits);
            organisationUnitData.updateUnit(organisationUnit);
            JOptionPane.showMessageDialog(getContentPane(), "Successfully updated credits :)");

        } else {
            JOptionPane.showMessageDialog(getContentPane(), "Error: Please enter a number greater than 0");

        }

    }

    private void addUser() {
        OrganisationUnit organisationUnit = (OrganisationUnit) orgUnitTableModel.getValueAt(orgUnitsTable.getSelectedRow(), 0);
        new AddOrgUserFrame(organisationUnit.getID(), new OrgUnitEmployeesData(new UnitEmployeeNDS()), new UsersData(new UsersNDS()));
    }

    private void newOrgUnitFrame () {
        new NewOrgUnitFrame(new OrganisationUnitData(new OrganisationUnitNDS()));
    }
}
