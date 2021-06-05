package com.company.GUI.TradingGUI;

import com.company.Model.Asset;
import com.company.Model.Bid;

import javax.swing.table.AbstractTableModel;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

public class BidTableModel extends AbstractTableModel {

    private ArrayList<Object[]> bidList;
    private String[] sellingColumnNames = {"Bid ID" ,"Asset", "Status", "Unit Price", "Total Quantity", "Sold Quantity", "Date"};
    private String[] buyingColumnNames = {"Bid ID" ,"Asset", "Status", "Unit Price", "Total Quantity", "Bought Quantity", "Date"};


    public void setData(ArrayList<Object[]> data) {
        bidList = data;
        fireTableDataChanged();
    }


    @Override
    public String getColumnName(int col) {
        if (bidList.size() > 0) {
            Bid bid = (Bid) bidList.get(0)[0];
            if (bid.getBuyType()) {
                return buyingColumnNames[col];
            } else {
                return sellingColumnNames[col];
            }
        }
        return "";
    }

    @Override
    public int getRowCount() {
        return bidList.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        int values = bidList.size();

        Bid[] bids = new Bid[values];
        Asset[] assets = new Asset[values];
        String[] status = new String[values];
        Double[] prices = new Double[values];
        Double[] quantities = new Double[values];
        Double[] activeQuantities = new Double[values];

        Date[] dates = new Date[values];

        for (int i = 0; i < values; i++) {
            bids[i] = (Bid) bidList.get(i)[0];
            assets[i] = (Asset) bidList.get(i)[1];
            status[i] = (String) bidList.get(i)[2];
            prices[i] = (Double) bidList.get(i)[3];
            quantities[i] = (Double) bidList.get(i)[4];
            activeQuantities[i] = (Double) bidList.get(i)[5];
            dates[i] = (Date) bidList.get(i)[6];
        }

        Bid bid = bids[rowIndex];
        Asset asset = assets[rowIndex];
        String bidStatus = status[rowIndex];
        Double price = prices[rowIndex];
        Double quantity = quantities[rowIndex];
        Double activeQuantity = activeQuantities[rowIndex];
        Date date = dates[rowIndex];

        switch (columnIndex) {
            case 0:
                return bid;
            case 1:
                return asset;
            case 2:
                return bidStatus;
            case 3:
                return price;
            case 4:
                return quantity;
            case 5:
                return activeQuantity;
            case 6:
                return date;
        }

        return null;
    }
}
