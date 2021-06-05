package com.company.Client.GUI.TradingGUI.SellGUI;

import com.company.Client.Client;
import com.company.Client.Data.BidData;
import com.company.Client.Data.OrgAssetData;
import com.company.Client.GUI.TradingGUI.BidTableModel;
import com.company.Common.NetworkDataSource.BidNDS;
import com.company.Common.NetworkDataSource.OrgAssetNDS;

import javax.swing.*;
import java.awt.*;

public class SellPanel extends JPanel {

    private JButton sellAssetBtn;
    private JTable sellOrdersTable;
    private JButton refreshTableButton;

    private BidTableModel bidTableModel;
    private BidData bidData;

    public SellPanel(BidData bidData) {

        this.bidData = bidData;

        bidTableModel = new BidTableModel();
        try {
            bidTableModel.setData(this.bidData.getBidList(Client.getLoggedInOrgID(), false));
        } catch (Exception e) {
            e.printStackTrace();
        }

        sellOrdersTable = new JTable(bidTableModel);
        sellOrdersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        sellAssetBtn = new JButton("New Sell Order");
        sellAssetBtn.addActionListener(e -> sellAsset());

        refreshTableButton = new JButton("Refresh Table");
        refreshTableButton.addActionListener(e -> refreshTable());

        setupLayout();

    }

    private void setupLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.insets = new Insets(10,10,10,10);

        constraints.gridy = 0;
        constraints.weighty = 1;
        add(new JScrollPane(sellOrdersTable), constraints);

        constraints.gridy = 1;
        constraints.weighty = 1;
        constraints.weightx = 1;
        add(refreshTableButton, constraints);

        constraints.gridy = 2;
        constraints.weighty = 1;
        constraints.weightx = 1;
        add(sellAssetBtn, constraints);

    }

    private void refreshTable() {
        try {
            bidTableModel.setData(this.bidData.getBidList(Client.getLoggedInOrgID(), false));
            sellOrdersTable.updateUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sellAsset() {
        new SellAssetFrame(new OrgAssetData(new OrgAssetNDS()), new BidData(new BidNDS()));
    }

}
