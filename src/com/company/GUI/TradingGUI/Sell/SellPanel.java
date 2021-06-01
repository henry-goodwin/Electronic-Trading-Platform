package com.company.GUI.TradingGUI.Sell;

import com.company.Database.Bids.BidData;
import com.company.Database.OrgUnitAssets.OrgAssetData;
import com.company.NetworkDataSource.BidNDS;
import com.company.NetworkDataSource.OrgAssetNDS;

import javax.swing.*;
import java.awt.*;

public class SellPanel extends JPanel {

    private JButton sellAssetBtn;

    public SellPanel() {

        sellAssetBtn = new JButton("New Sell Order");
        sellAssetBtn.addActionListener(e -> sellAsset());

        setupLayout();

    }

    private void setupLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;

        constraints.gridy = 0;
        constraints.weighty = 1;
        add(sellAssetBtn, constraints);
    }

    private void sellAsset() {
        new SellAssetFrame(new OrgAssetData(new OrgAssetNDS()), new BidData(new BidNDS()));
    }

}
