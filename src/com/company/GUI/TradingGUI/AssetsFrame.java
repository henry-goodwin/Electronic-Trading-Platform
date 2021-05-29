package com.company.GUI.TradingGUI;

import com.company.Database.Assets.AssetData;
import com.company.Database.OrgUnitAssets.OrgAssetData;
import com.company.Database.Users.UsersData;
import com.company.NetworkDataSource.AssetNDS;
import com.company.NetworkDataSource.OrgAssetNDS;

import javax.swing.*;
import java.awt.*;

public class AssetsFrame extends JFrame {


    private JPanel manageAssetsPanel;

    private JTable assetsTable;
    private OrgAssetTableModel orgAssetTableModel;

    private JButton addButton;
    private JButton editButton;
    private JButton removeButton;

    private OrgAssetData orgAssetData;

    public AssetsFrame(OrgAssetData orgAssetData) {
        super("Manage Assets");
        setDefaultLookAndFeelDecorated(true);
        setLayout(new BorderLayout());

        this.orgAssetData = orgAssetData;

        orgAssetTableModel = new OrgAssetTableModel();
        orgAssetTableModel.setData(this.orgAssetData.getAssetList(1));

        manageAssetsPanel = new JPanel();
        add(manageAssetsPanel, BorderLayout.CENTER);

        assetsTable = new JTable(orgAssetTableModel);

        addButton = new JButton("Add Asset");
        addButton.addActionListener(e -> {
            new NewAssetFrame(new AssetData(new AssetNDS()), new OrgAssetData(new OrgAssetNDS(), 1));
        });
        editButton = new JButton("Edit Asset");
        removeButton = new JButton("Remove Asset");

        setupLayout();
        setSize(500,500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationByPlatform(true);
        setVisible(true);

    }

    private void setupLayout() {
        manageAssetsPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;

        constraints.gridy = 0;
        constraints.gridwidth = 3;
        constraints.weighty = 1;
        manageAssetsPanel.add(new JScrollPane(assetsTable), constraints);

        constraints.gridy = 1;
        constraints.weightx = 1;
        manageAssetsPanel.add(addButton, constraints);

        constraints.gridy = 2;
        manageAssetsPanel.add(editButton, constraints);

        constraints.gridy = 3;
        manageAssetsPanel.add(removeButton, constraints);
    }


}
