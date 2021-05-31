package com.company.Database.OrgUnitAssets;

import com.company.Client;
import com.company.Model.OrgAsset;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class OrgAssetData {
    private DefaultListModel<OrgAsset> orgAssetDefaultListModel;
    private OrgUnitAssetDataSource orgUnitAssetDataSource;
    private OrgAsset orgAssetTableData[][];
    private TableModel tableModel;
    private Integer orgID;

    public OrgAssetData(OrgUnitAssetDataSource dataSource) {
        orgUnitAssetDataSource = dataSource;
        orgAssetDefaultListModel = new DefaultListModel<OrgAsset>();
        this.orgID = Client.getLoggedInOrgID();

        for (OrgAsset orgAsset: orgUnitAssetDataSource.myOrgAssetSet(orgID)) {
            orgAssetDefaultListModel.addElement(orgAsset);
        }
    }

//    public OrgAssetData(OrgUnitAssetDataSource dataSource) {
//        orgUnitAssetDataSource = dataSource;
//    }

    /**
     * Saves the data in the database using a persistence
     * mechanism.
     */
    public void persist() {
        orgUnitAssetDataSource.close();
    }

    /**
     * Retrieves OrgAsset details from the model.
     *
     * @return the OrgAsset object related to the id.
     */
    public OrgAsset get(Integer orgID, Integer assetID) {
        return orgUnitAssetDataSource.getOrgAsset(orgID, assetID);
    }

    /**
     * Accessor for the list model.
     *
     * @return the listModel to display.
     */
    public ListModel<OrgAsset> getOrgAssetModel() { return  orgAssetDefaultListModel; }

    /**
     * Adds a OrgAsset to the database.
     *
     * @param orgAsset OrgAsset to add to the database.
     */
    public void addOrgAsset(OrgAsset orgAsset) {
        orgUnitAssetDataSource.addAsset(orgAsset);
    }

    public Boolean checkAsset(Integer orgID, Integer assetID) {
        return orgUnitAssetDataSource.checkAsset(orgID, assetID);
    }

    public ArrayList<Object[]> getAssetList(Integer orgID) {
        return orgUnitAssetDataSource.getAssetList(orgID);
    }

    public void updateQuantity(Integer orgID, Integer assetID ,Double quantity) {
        orgUnitAssetDataSource.updateQuantity(orgID, assetID, quantity);
    }
}
