package com.company.GUI.TradingGUI;

import com.company.Model.Asset;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

public class OrgAssetTableModel extends AbstractTableModel {

    private ArrayList<Object[]> orgAssetList;
    private String[] columnNames = { "Name", "Quantity"};

    public void setData(ArrayList<Object[]> data) {
        orgAssetList = data;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public int getRowCount() {
        return orgAssetList.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int row, int col) {

        int values = orgAssetList.size();

        String[] assetNames = new String[values];
        String[] assetQuantities = new String[values];

        for (int i = 0; i < values; i++) {
            assetNames[i] = String.valueOf((Asset) orgAssetList.get(i)[0]);
            assetQuantities[i] = String.valueOf((Double) orgAssetList.get(i)[1]);
        }

        String name = assetNames[row];
        String quantity = assetQuantities[row];

        switch (col) {
            case 0:
                return name;
            case 1:
                return quantity;
        }

        return null;
    }

}
