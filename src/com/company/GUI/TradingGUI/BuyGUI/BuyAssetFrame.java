package com.company.GUI.TradingGUI.BuyGUI;

import com.company.Client;
import com.company.Database.Assets.AssetData;
import com.company.Database.Bids.BidData;
import com.company.GUI.TradingGUI.SellGUI.SellAssetFrame;
import com.company.Model.Asset;
import com.company.Model.Bid;
import com.company.Model.OrgAsset;

import javax.swing.*;
import java.awt.*;

public class BuyAssetFrame extends JFrame {

    private JPanel buyPanel;

    private JList assetsList;
    private JTextField quantityField;
    private JTextField priceField;
    private JButton placeBuyOrderBtn;

    private AssetData assetData;
    private BidData bidData;

    public BuyAssetFrame(AssetData assetData, BidData bidData) {
        super("Place buy order");
        setDefaultLookAndFeelDecorated(true);
        setLayout(new BorderLayout());

        this.assetData = assetData;
        this.bidData = bidData;

        assetsList = new JList(assetData.getAssetModel());
        quantityField = new JTextField();
        priceField = new JTextField();
        placeBuyOrderBtn = new JButton("Place Buy Order");
        placeBuyOrderBtn.addActionListener(e -> buyAsset());

        buyPanel = new JPanel();
        add(buyPanel, BorderLayout.CENTER);

        setupLayout();

        setSize(500,500);
        setLocationByPlatform(true);
        setVisible(true);

    }

    private void setupLayout() {

        buyPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.insets = new Insets(10,10,10,10);
        constraints.gridy = 0;
        constraints.weighty = 1;
        constraints.weightx = 1;
        buyPanel.add(new JScrollPane(assetsList), constraints);

        constraints.gridy = 1;
        constraints.weighty = 1;
        constraints.weightx = 1;
        buyPanel.add(new JLabel("Quantity"), constraints);

        constraints.gridy = 2;
        constraints.weighty = 1;
        constraints.weightx = 1;
        buyPanel.add(quantityField, constraints);

        constraints.gridy = 3;
        constraints.weighty = 1;
        constraints.weightx = 1;
        buyPanel.add(new JLabel("Price"), constraints);

        constraints.gridy = 4;
        constraints.weighty = 1;
        constraints.weightx = 1;
        buyPanel.add(priceField, constraints);

        constraints.gridy = 5;
        constraints.weighty = 1;
        constraints.weightx = 1;
        buyPanel.add(placeBuyOrderBtn, constraints);

    }

    private void buyAsset() {
        if (quantityField.getText().equals("") ||
                priceField.getText().equals("") ||
                assetsList.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(getContentPane(), "Error: Please ensure all fields are valid");
        } else {
            Double quantity = Double.parseDouble(quantityField.getText());
            Double price = Double.parseDouble(priceField.getText());
            Asset asset = (Asset) assetsList.getSelectedValue();

            Bid bid = new Bid(asset.getAssetID(), Client.getLoggedInOrgID(), "open", true, price, quantity);
            bidData.addBid(bid);
            JOptionPane.showMessageDialog(getContentPane(), "Successfully added new buy bid :)");
            BuyAssetFrame.this.dispose();
        }
    }
}
