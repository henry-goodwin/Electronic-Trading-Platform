package com.company.GUI.AdminGUI;

import com.company.Database.Assets.AssetData;
import com.company.Model.Asset;
import com.company.NetworkDataSource.AssetNDS;

import javax.swing.*;
import java.awt.*;

public class ManageAssetsFrame extends JFrame {

    private JPanel assetsPanel;
    private JList assetsList;

    private JButton addAssetButton;
    private JButton editAssetButton;
    private JButton deleteAssetButton;

    private AssetData assetData;
    private DefaultListModel<Asset> listModel;


    public ManageAssetsFrame(AssetData assetData) {

        super("Manage Assets");

        setDefaultLookAndFeelDecorated(true);
        setLayout(new BorderLayout());

        this.assetData = assetData;
        this.listModel = (DefaultListModel<Asset>) assetData.getAssetModel();

        assetsPanel = new JPanel();
        assetsList = new JList(listModel);
        addAssetButton = new JButton("Add Asset");
        editAssetButton = new JButton("Edit Asset");
        deleteAssetButton = new JButton("Delete Asset");

        addAssetButton.addActionListener(e -> addAsset());
        editAssetButton.addActionListener(e -> editAsset());
        deleteAssetButton.addActionListener(e -> deleteAsset());

        add(assetsPanel, BorderLayout.CENTER);

        setupLayout();

        setSize(500,500);
        setLocationByPlatform(true);
        setVisible(true);

    }

    private void setupLayout() {

        assetsPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.weighty = 1;
        constraints.weightx = 1;

        constraints.gridy = 0;
        assetsPanel.add(new JScrollPane(assetsList), constraints);

        constraints.gridy = 1;
        assetsPanel.add(addAssetButton, constraints);

        constraints.gridy = 2;
        assetsPanel.add(editAssetButton, constraints);

        constraints.gridy = 3;
        assetsPanel.add(deleteAssetButton, constraints);

    }

    private void addAsset() {

        String name = JOptionPane.showInputDialog(getContentPane(),"Asset Name");

        // Check that name hasn't been used
        try {
            if (assetData.nameAvailability(name)) {
                try {
                    assetData.addAsset(new Asset(name));
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(getContentPane(), e.getMessage());
                }

                this.assetData = new AssetData(new AssetNDS());
                listModel = (DefaultListModel<Asset>) assetData.getAssetModel();
                assetsList.setModel(listModel);
                updateList();

            } else {
                JOptionPane.showMessageDialog(getContentPane(), "Error: Asset Already exists");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(getContentPane(), e.getMessage());
        }
    }

    private void editAsset() {

        if (assetsList.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(getContentPane(), "Error: Please select a asset");
        } else {
            String newName = JOptionPane.showInputDialog(getContentPane(),"Enter new asset name");
            Asset selectedAsset = (Asset) assetsList.getSelectedValue();
            try {
                assetData.updateAssetName(selectedAsset.getAssetID(), newName);
                updateList();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(getContentPane(), e.getMessage());
            }

        }

    }

    private void deleteAsset() {
        if (assetsList.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(getContentPane(), "Error: Please select a asset");
        } else {

            Asset selectedAsset = (Asset) assetsList.getSelectedValue();
            try {
                assetData.deleteAsset(selectedAsset.getAssetID());
                updateList();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(getContentPane(), e.getMessage());
            }

        }
    }

    private void updateList() {
        try {
            this.assetData = new AssetData(new AssetNDS());
            listModel = (DefaultListModel<Asset>) assetData.getAssetModel();
            assetsList.setModel(listModel);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(getContentPane(), e.getMessage());
        }
    }
}
