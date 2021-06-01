package com.company.GUI.TradingGUI.Sell;

import com.company.Client;
import com.company.Database.Bids.BidData;
import com.company.Database.OrgUnitAssets.OrgAssetData;
import com.company.Model.Asset;
import com.company.Model.Bid;
import com.company.Model.OrgAsset;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class SellAssetFrame extends JFrame {

    private JPanel sellPanel;

    private JList assetsJList;
    private JTextField quantityField;
    private JTextField priceField;
    private JButton placeSellOrder;

    private OrgAssetData orgAssetData;
    private BidData bidData;

    public SellAssetFrame(OrgAssetData orgAssetData, BidData bidData) {
        super("Place sell order");
        setDefaultLookAndFeelDecorated(true);
        setLayout(new BorderLayout());

        this.orgAssetData = orgAssetData;
        this.bidData = bidData;

        assetsJList = new JList(orgAssetData.getOrgAssetModel());
        quantityField = new JTextField();
        priceField = new JTextField();
        placeSellOrder = new JButton("Place Sell Order");
        placeSellOrder.addActionListener(e -> sellAsset());

        sellPanel = new JPanel();
        add(sellPanel, BorderLayout.CENTER);

        setupLayout();

        setSize(500,500);
        setLocationByPlatform(true);
        setVisible(true);
    }

    private void setupLayout() {
        sellPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;

        constraints.gridy = 0;
        constraints.weighty = 1;
        sellPanel.add(new JScrollPane(assetsJList), constraints);

        constraints.gridy = 1;
        constraints.weighty = 1;
        sellPanel.add(new JLabel("Quantity"), constraints);

        constraints.gridy = 2;
        constraints.weighty = 1;
        sellPanel.add(quantityField, constraints);

        constraints.gridy = 3;
        constraints.weighty = 1;
        sellPanel.add(new JLabel("Price"), constraints);

        constraints.gridy = 4;
        constraints.weighty = 1;
        sellPanel.add(priceField, constraints);

        constraints.gridy = 5;
        constraints.weighty = 1;
        sellPanel.add(placeSellOrder, constraints);

    }

    private void sellAsset() {

        if (quantityField.getText().equals("") ||
                priceField.getText().equals("") ||
                assetsJList.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(getContentPane(), "Error: Please ensure all fields are valid");
        } else {
            Double quantity = Double.parseDouble(quantityField.getText());
            Double price = Double.parseDouble(priceField.getText());
            OrgAsset asset = (OrgAsset) assetsJList.getSelectedValue();

            Bid bid = new Bid(asset.getAssetID(), Client.getLoggedInOrgID(), "open", false, price, quantity);
            bidData.addBid(bid);
            JOptionPane.showMessageDialog(getContentPane(), "Successfully added new sell bid :)");
            SellAssetFrame.this.dispose();
        }

    }
}
