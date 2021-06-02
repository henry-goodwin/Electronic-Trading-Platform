package com.company.GUI.TradingGUI;

import com.company.Model.Asset;
import com.company.Model.OrgAsset;

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

        OrgAsset[] assetNames = new OrgAsset[values];
        Double[] assetQuantities = new Double[values];

        for (int i = 0; i < values; i++) {
            assetNames[i] = (OrgAsset) orgAssetList.get(i)[0];
            assetQuantities[i] = (Double) orgAssetList.get(i)[1];
        }

        OrgAsset asset = assetNames[row];
        Double quantity = assetQuantities[row];

        switch (col) {
            case 0:
                return asset;
            case 1:
                return quantity;
        }

        return null;
    }

}
