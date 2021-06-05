package com.company.GUI.TradingGUI;

import com.company.Client;
import com.company.Database.Assets.AssetData;
import com.company.Database.Assets.AssetDataSource;
import com.company.Database.OrgUnitAssets.OrgAssetData;
import com.company.Database.Users.UsersData;
import com.company.GUI.AdminGUI.ManageOrganisationUnitGUI.NewOrgUnitFrame;
import com.company.Model.Asset;
import com.company.Model.OrgAsset;
import com.company.Model.Person;
import com.company.NetworkDataSource.AssetNDS;

import javax.swing.*;
import java.awt.*;

public class NewAssetFrame extends JFrame {
    private JPanel newAssetsPanel;

    private JList namesList;
    private JTextField quantityField;
    private JButton addNewAssetButton;
//    private JButton newNameButton;

    private AssetData assetData;
    private DefaultListModel<Asset> listModel;

    private OrgAssetData orgAssetData;

    public NewAssetFrame(AssetData usersData, OrgAssetData orgAssetData) {
        super("New Assets");
        setDefaultLookAndFeelDecorated(true);
        setLayout(new BorderLayout());

        this.assetData = usersData;
        this.orgAssetData = orgAssetData;

        listModel = (DefaultListModel<Asset>) assetData.getAssetModel();
        namesList = new JList(listModel);
        quantityField = new JTextField();
        addNewAssetButton = new JButton("Add New Asset to Organisation");
        addNewAssetButton.addActionListener(e -> addAsset());
        newAssetsPanel = new JPanel();
        add(newAssetsPanel, BorderLayout.CENTER);


        setupLayout();

        setSize(500,500);
        setLocationByPlatform(true);
        setVisible(true);

    }

    private void setupLayout() {
        newAssetsPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.weighty = 1;
        constraints.weightx = 1;

        constraints.gridy = 0;
        newAssetsPanel.add(new JScrollPane(namesList), constraints);

        constraints.gridy = 1;
        newAssetsPanel.add(new JLabel("Quantity"), constraints);

        constraints.gridy = 2;
        newAssetsPanel.add(quantityField, constraints);

        constraints.gridy = 3;
        newAssetsPanel.add(addNewAssetButton, constraints);
    }


    private void addAsset() {
        if (namesList.isSelectionEmpty() || quantityField.getText().equals("")) {
            JOptionPane.showMessageDialog(getContentPane(), "Error: Please ensure all fields are valid");
        } else {
            // Check if org already has this asset
            Asset asset = (Asset) namesList.getSelectedValue();
            Integer orgID = Client.getLoggedInOrgID();
            // returns true if asset is available
            try {
                if (orgAssetData.checkAsset(orgID, asset.getAssetID())) {
                    Double quantity = Double.parseDouble(quantityField.getText());
                    try {
                        orgAssetData.addOrgAsset(new OrgAsset(orgID,
                                asset.getAssetID(),
                                quantity));
                        JOptionPane.showMessageDialog(getContentPane(), "Successfully added new organisation unit asset :)");
                        NewAssetFrame.this.dispose();

                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(getContentPane(), e.getMessage());
                    }

                } else {
                    JOptionPane.showMessageDialog(getContentPane(), "Error: Asset Already exists, please edit instead");
                }
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(getContentPane(), e.getMessage());
            }
        }
    }

}
