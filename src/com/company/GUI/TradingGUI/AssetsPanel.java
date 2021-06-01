package com.company.GUI.TradingGUI;

import com.company.Client;
import com.company.Database.Assets.AssetData;
import com.company.Database.OrgUnitAssets.OrgAssetData;
import com.company.Database.OrganisationUnit.OrganisationUnitData;
import com.company.Database.Persons.PersonsData;
import com.company.Database.Users.UsersData;
import com.company.GUI.LoginGUI.LoginFrame;
import com.company.Model.Asset;
import com.company.Model.OrgAsset;
import com.company.NetworkDataSource.AssetNDS;
import com.company.NetworkDataSource.OrgAssetNDS;
import com.company.NetworkDataSource.UsersNDS;

import javax.swing.*;
import java.awt.*;

public class AssetsPanel extends JPanel {

    private JTable assetsTable;
    private OrgAssetTableModel orgAssetTableModel;

    private JButton addButton;
    private JButton editButton;
    private JButton removeButton;
    private JButton refreshTableButton;
    private JLabel nameLabel;

    private OrgAssetData orgAssetData;
    private OrganisationUnitData organisationUnitData;
    private PersonsData personsData;


    public AssetsPanel(OrgAssetData orgAssetData, OrganisationUnitData organisationUnitData, PersonsData personsData) {

        this.orgAssetData = orgAssetData;
        this.organisationUnitData = organisationUnitData;
        this.personsData = personsData;

        orgAssetTableModel = new OrgAssetTableModel();
        orgAssetTableModel.setData(this.orgAssetData.getAssetList(1));

        String orgName = organisationUnitData.get(Client.getLoggedInOrgID()).getName();
        String fullName = personsData.get(Client.getLoggedInPersonID()).toString();

        String welcomeString = "Name: " + fullName + " || Organisational Unit: " + orgName;
        nameLabel = new JLabel(welcomeString);

        assetsTable = new JTable(orgAssetTableModel);
        assetsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        refreshTableButton = new JButton("Reload Data");
        refreshTableButton.addActionListener(e -> orgAssetTableModel.fireTableDataChanged());

        addButton = new JButton("Add Asset");
        addButton.addActionListener(e -> {
            new NewAssetFrame(new AssetData(new AssetNDS()), new OrgAssetData(new OrgAssetNDS()));
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
        add(nameLabel, constraints);

        constraints.gridy = 1;
        constraints.weighty = 1;
        add(new JScrollPane(assetsTable), constraints);

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

        Asset asset = (Asset) orgAssetTableModel.getValueAt(assetsTable.getSelectedRow(), 0);
        Double quantity = (Double) orgAssetTableModel.getValueAt(assetsTable.getSelectedRow(), 1);

        JFrame superFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        String newQuantity = JOptionPane.showInputDialog(superFrame.getContentPane(),"Enter new quantity");
        orgAssetData.updateQuantity(1, asset.getAssetID() ,Double.parseDouble(newQuantity));

    }


}
