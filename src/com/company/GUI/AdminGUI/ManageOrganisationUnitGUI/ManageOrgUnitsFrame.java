package com.company.GUI.AdminGUI.ManageOrganisationUnitGUI;

import com.company.Database.OrgUnitEmployees.OrgUnitEmployeesData;
import com.company.Database.OrganisationUnit.OrganisationUnitData;
import com.company.Database.Users.UsersData;
import com.company.GUI.AdminGUI.OrgUnitTableModel;
import com.company.Model.OrganisationUnit;
import com.company.NetworkDataSource.OrganisationUnitNDS;
import com.company.NetworkDataSource.UnitEmployeeNDS;
import com.company.NetworkDataSource.UsersNDS;
import com.company.Testing.TestingException;

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

    private JButton updateTableBtn;
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
        try {
            orgUnitTableModel.setData(this.organisationUnitData.getList());
        } catch (Exception e) {
            e.printStackTrace();
        }

        orgUnitsTable = new JTable(orgUnitTableModel);
        orgUnitsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        editCreditsBtn = new JButton("Edit Credits");
        editCreditsBtn.addActionListener(e -> editCredits());

        updateTableBtn = new JButton("Update Table");
        updateTableBtn.addActionListener(e -> updateTable());

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
        manageOrgPanel.add(updateTableBtn, constraints);

        constraints.gridy = 2;
        constraints.weighty = 1;
        constraints.weightx = 1;
        manageOrgPanel.add(editCreditsBtn, constraints);

        constraints.gridy = 3;
        constraints.weighty = 1;
        constraints.weightx = 1;
        manageOrgPanel.add(addUsersBtn, constraints);
    }

    private void editCredits() {

        String rawCredits = (JOptionPane.showInputDialog(getContentPane(),"Enter New Credit Amount"));

        if(!rawCredits.matches("[0-9]+")){
            JOptionPane.showMessageDialog(getContentPane(), "Error: Please enter a valid number");
        }else {
            Double credits = Double.valueOf(rawCredits);
                OrganisationUnit organisationUnit = (OrganisationUnit) orgUnitTableModel.getValueAt(orgUnitsTable.getSelectedRow(), 0);
                try {
                    organisationUnit.setCredits(credits);
                    organisationUnitData.updateUnit(organisationUnit);
                    JOptionPane.showMessageDialog(getContentPane(), "Successfully updated credits :)");
                    updateTable();

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(getContentPane(), e.getMessage());
                }
        }
    }

    private void addUser() {
        OrganisationUnit organisationUnit = (OrganisationUnit) orgUnitTableModel.getValueAt(orgUnitsTable.getSelectedRow(), 0);
        try {
            new AddOrgUserFrame(organisationUnit.getID(), new OrgUnitEmployeesData(new UnitEmployeeNDS()), new UsersData(new UsersNDS()));
        } catch (TestingException e) {
            e.printStackTrace();
        }
    }

    private void newOrgUnitFrame () {
        new NewOrgUnitFrame(new OrganisationUnitData(new OrganisationUnitNDS()));
    }

    private void updateTable(){
        try {
            orgUnitTableModel.setData(this.organisationUnitData.getList());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
