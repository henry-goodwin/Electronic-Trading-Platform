package com.company.Client.Data;

import com.company.Common.DataSource.OrgUnitAssetDataSource;
import com.company.Common.Model.OrgAsset;

import javax.swing.*;
import java.util.ArrayList;

public class OrgAssetData {
    private DefaultListModel<OrgAsset> orgAssetDefaultListModel;
    private OrgUnitAssetDataSource orgUnitAssetDataSource;


    public OrgAssetData(OrgUnitAssetDataSource dataSource) {
        orgUnitAssetDataSource = dataSource;
        orgAssetDefaultListModel = new DefaultListModel<OrgAsset>();

    }

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
    public OrgAsset get(Integer orgID, Integer assetID) throws Exception {
        return orgUnitAssetDataSource.getOrgAsset(orgID, assetID);
    }

    /**
     * Adds a OrgAsset to the database.
     *
     * @param orgAsset OrgAsset to add to the database.
     */
    public void addOrgAsset(OrgAsset orgAsset) throws Exception {
        orgUnitAssetDataSource.addAsset(orgAsset);
    }

    public Boolean checkAsset(Integer orgID, Integer assetID) throws Exception {
        return orgUnitAssetDataSource.checkAsset(orgID, assetID);
    }

    public ArrayList<Object[]> getAssetList(Integer orgID) throws Exception {
        return orgUnitAssetDataSource.getAssetList(orgID);
    }

    public void updateQuantity(Integer orgID, Integer assetID ,Double quantity) throws Exception {
        orgUnitAssetDataSource.updateQuantity(orgID, assetID, quantity);
    }

    public void updateOrgAsset(OrgAsset orgAsset) throws Exception { orgUnitAssetDataSource.updateOrgAsset(orgAsset); }

    public void updateData(Integer orgUnitID, Integer assetID, Double quantity) throws Exception {
        orgUnitAssetDataSource.updateData(orgUnitID, assetID, quantity);
    }
}