package com.company.GUI.TradingGUI;

import com.company.Client;
import com.company.Database.Assets.AssetData;
import com.company.Database.Bids.BidData;
import com.company.Database.OrgUnitAssets.OrgAssetData;
import com.company.Database.Users.UsersData;
import com.company.GUI.UserGUI.ChangePasswordFrame;
import com.company.Model.Asset;
import com.company.Model.OrgAsset;
import com.company.NetworkDataSource.AssetNDS;
import com.company.NetworkDataSource.BidNDS;
import com.company.NetworkDataSource.OrgAssetNDS;
import com.company.Utilities.PasswordHasher;

import javax.swing.*;
import java.awt.*;

public class AssetsPanel extends JPanel {

    private JTable assetsTable;
    private OrgAssetTableModel orgAssetTableModel;

    private JButton seePriceHistoryButton;
    private JButton addButton;
    private JButton editButton;
    private JButton removeButton;
    private JButton refreshTableButton;

    private OrgAssetData orgAssetData;


    public AssetsPanel(OrgAssetData orgAssetData) {

        this.orgAssetData = orgAssetData;

        orgAssetTableModel = new OrgAssetTableModel();

        try {
            orgAssetTableModel.setData(this.orgAssetData.getAssetList(Client.getLoggedInOrgID()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        assetsTable = new JTable(orgAssetTableModel);
        assetsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        refreshTableButton = new JButton("Reload Data");
        refreshTableButton.addActionListener(e -> {
            try {
                orgAssetTableModel.setData(this.orgAssetData.getAssetList(Client.getLoggedInOrgID()));
                assetsTable.updateUI();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        seePriceHistoryButton = new JButton("See Asset Price History");
        seePriceHistoryButton.addActionListener(e -> seePriceHistory());

        addButton = new JButton("Add Asset");
        addButton.addActionListener(e -> {
            try {
                new NewAssetFrame(new AssetData(new AssetNDS()), new OrgAssetData(new OrgAssetNDS()));
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        editButton = new JButton("Edit Asset");
        editButton.addActionListener(e -> editAsset());

        removeButton = new JButton("Remove Asset");

        setupLayout();
    }

    private void setupLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;

        constraints.gridy = 0;
        constraints.weighty = 1;
        add(new JScrollPane(assetsTable), constraints);

        constraints.gridy = 1;
        constraints.weightx = 1;
        add(seePriceHistoryButton, constraints);

        constraints.gridy = 2;
        constraints.weightx = 1;
        add(refreshTableButton, constraints);

        constraints.gridy = 3;
        constraints.weightx = 1;
        add(addButton, constraints);

        constraints.gridy = 4;
        add(editButton, constraints);

        constraints.gridy = 5;
        add(removeButton, constraints);
    }

    private void editAsset() {

        OrgAsset asset = (OrgAsset) orgAssetTableModel.getValueAt(assetsTable.getSelectedRow(), 0);
        Double quantity = (Double) orgAssetTableModel.getValueAt(assetsTable.getSelectedRow(), 1);

        JFrame superFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        String newQuantity = JOptionPane.showInputDialog(superFrame.getContentPane(),"Enter new quantity");
        try {
            orgAssetData.updateQuantity(1, asset.getAssetID() ,Double.parseDouble(newQuantity));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(superFrame.getContentPane(), e.getMessage());
        }

    }

    private void seePriceHistory() {
        OrgAsset asset = (OrgAsset) orgAssetTableModel.getValueAt(assetsTable.getSelectedRow(), 0);

        EventQueue.invokeLater(() -> {

            PastHistoryGUI ex = null;
            try {
                ex = new PastHistoryGUI(new BidData(new BidNDS()), new AssetData(new AssetNDS()), asset.getAssetID());
                ex.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}
