package com.company.Client.GUI.AdminGUI;

import com.company.Common.Model.OrganisationUnit;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class OrgUnitTableModel extends AbstractTableModel {

    private ArrayList<Object[]> orgList;
    private String[] columnNames = { "Name", "Credits"};

    public void setData(ArrayList<Object[]> data) {
        orgList = data;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public int getRowCount() {
        return orgList.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int row, int col) {

        int values = orgList.size();

        OrganisationUnit[] orgNames = new OrganisationUnit[values];
        Double[] credits = new Double[values];

        for (int i = 0; i < values; i++) {
            orgNames[i] = (OrganisationUnit) orgList.get(i)[0];
            credits[i] = (Double) orgList.get(i)[1];
        }

        OrganisationUnit organisationUnit = orgNames[row];
        Double credit = credits[row];

        switch (col) {
            case 0:
                return organisationUnit;
            case 1:
                return credit;
        }

        return null;
    }

}