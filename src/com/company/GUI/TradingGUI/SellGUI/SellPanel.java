package com.company.GUI.TradingGUI.SellGUI;

import com.company.Client;
import com.company.Database.Bids.BidData;
import com.company.Database.OrgUnitAssets.OrgAssetData;
import com.company.GUI.TradingGUI.BidTableModel;
import com.company.GUI.TradingGUI.OrgAssetTableModel;
import com.company.NetworkDataSource.BidNDS;
import com.company.NetworkDataSource.OrgAssetNDS;

import javax.swing.*;
import java.awt.*;

public class SellPanel extends JPanel {

    private JButton sellAssetBtn;
    private JTable sellOrdersTable;
    private BidTableModel bidTableModel;
    private JButton checkTrades;

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

        checkTrades = new JButton("Check trades");
        checkTrades.addActionListener(e -> {
            try {
                bidData.checkTrades();
            } catch (Exception exception) {
            // Print fail
            }
        });

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
        add(sellAssetBtn, constraints);

        constraints.gridy = 2;
        constraints.weighty = 1;
        constraints.weightx = 1;
        add(checkTrades, constraints);
    }

    private void sellAsset() {
        new SellAssetFrame(new OrgAssetData(new OrgAssetNDS()), new BidData(new BidNDS()));
    }

}
