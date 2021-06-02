package com.company.GUI.TradingGUI.BuyGUI;

import com.company.Database.Assets.AssetData;
import com.company.Database.Bids.BidData;
import com.company.NetworkDataSource.AssetNDS;
import com.company.NetworkDataSource.BidNDS;

import javax.swing.*;
import java.awt.*;

public class BuyPanel extends JPanel {

    private JButton buyAssetButton;

    public BuyPanel() {

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
        constraints.weightx = 1;
        add(buyAssetButton, constraints);
    }

    private static void buyAsset() { new BuyAssetFrame(new AssetData(new AssetNDS()), new BidData(new BidNDS())); }
}
