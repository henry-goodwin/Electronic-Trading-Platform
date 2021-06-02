package com.company.GUI.AdminGUI.ManageOrganisationUnitGUI;

import com.company.Database.OrgUnitEmployees.OrgUnitEmployeesData;
import com.company.Database.OrganisationUnit.OrganisationUnitData;
import com.company.Database.Users.UsersData;
import com.company.GUI.AdminGUI.OrgUnitTableModel;
import com.company.Model.OrganisationUnit;
import com.company.Model.UnitEmployee;
import com.company.Model.User;

import javax.swing.*;
import java.awt.*;

public class AddOrgUserFrame extends JFrame {

    private OrgUnitEmployeesData orgUnitEmployeesData;
    private UsersData usersData;
    private Integer orgID;

    private JPanel newEmployeePanel;
    private JList userList;
    private JButton addButton;


    public AddOrgUserFrame(Integer orgID, OrgUnitEmployeesData orgUnitEmployeesData, UsersData usersData) {
        super("Add new employee to " + orgID);
        setDefaultLookAndFeelDecorated(true);
        setLayout(new BorderLayout());
        this.orgUnitEmployeesData = orgUnitEmployeesData;
        this.usersData = usersData;
        this.orgID = orgID;

        userList = new JList(usersData.getModel());
        addButton = new JButton("Add Employee");
        addButton.addActionListener(e -> addEmployee());
        newEmployeePanel = new JPanel();
        add(newEmployeePanel, BorderLayout.CENTER);

        setupLayout();

        setSize(500,500);
        setLocationByPlatform(true);
        setVisible(true);

    }

    private void setupLayout() {

        newEmployeePanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.insets = new Insets(10,10,10,10);

        constraints.gridy = 0;
        constraints.weighty = 1;
        constraints.weightx = 1;
        newEmployeePanel.add(new JScrollPane(userList), constraints);

        constraints.gridy = 1;
        constraints.weighty = 1;
        constraints.weightx = 1;
        newEmployeePanel.add(addButton, constraints);

    }

    private void addEmployee() {

        User user = (User) userList.getSelectedValue();
        UnitEmployee unitEmployee = new UnitEmployee(user.getUserID(), orgID);
        orgUnitEmployeesData.addEmployee(unitEmployee);
        JOptionPane.showMessageDialog(getContentPane(), "Successfully added unit " + orgID);

        AddOrgUserFrame.this.dispose();

    }

}
