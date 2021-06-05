package com.company.GUI.TradingGUI.BuyGUI;

import com.company.Client;
import com.company.Database.Assets.AssetData;
import com.company.Database.Bids.BidData;
import com.company.Database.OrganisationUnit.OrganisationUnitData;
import com.company.GUI.TradingGUI.BidTableModel;
import com.company.NetworkDataSource.AssetNDS;
import com.company.NetworkDataSource.BidNDS;
import com.company.NetworkDataSource.OrganisationUnitNDS;

import javax.swing.*;
import java.awt.*;

public class BuyPanel extends JPanel {

    private JButton buyAssetButton;
    private JButton refreshTableButton;
    private JTable buyOrdersTable;
    private BidTableModel buyTableModel;

    private BidData bidData;


    public BuyPanel(BidData bidData) {

        this.bidData = bidData;

        buyTableModel = new BidTableModel();
        try {
            buyTableModel.setData(this.bidData.getBidList(Client.getLoggedInOrgID(), true));
        } catch (Exception e) {
            e.printStackTrace();
        }

        buyOrdersTable = new JTable(buyTableModel);
        buyOrdersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        refreshTableButton = new JButton("Refresh Table");
        refreshTableButton.addActionListener(e -> refreshTable());

        buyAssetButton = new JButton("New Buy Order");
        buyAssetButton.addActionListener(e -> buyAsset());

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
        add(new JScrollPane(buyOrdersTable), constraints);

        constraints.gridy = 1;
        constraints.weighty = 1;
        constraints.weightx = 1;
        add(refreshTableButton, constraints);

        constraints.gridy = 2;
        constraints.weighty = 1;
        constraints.weightx = 1;
        add(buyAssetButton, constraints);
    }

    private static void buyAsset() {
        try {
            new BuyAssetFrame(new AssetData(new AssetNDS()), new BidData(new BidNDS()), new OrganisationUnitData(new OrganisationUnitNDS()));
        } catch (Exception e) {
        }
    }

    private void refreshTable() {
        try {
            buyTableModel.setData(this.bidData.getBidList(Client.getLoggedInOrgID(), true));
            buyOrdersTable.updateUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
