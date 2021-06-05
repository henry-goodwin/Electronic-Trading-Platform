package com.company.Client.Data;

import com.company.Common.DataSource.AssetDataSource;
import com.company.Common.Model.Asset;

import javax.swing.*;

public class AssetData {

    private DefaultListModel<Asset> assetDefaultListModel;
    private AssetDataSource assetDataSource;

    /**
     * Constructor that initializes variables
     * @param dataSource
     */
    public AssetData(AssetDataSource dataSource) throws Exception {
        assetDataSource = dataSource;
        assetDefaultListModel = new DefaultListModel<Asset>();

        for (Asset asset : assetDataSource.assetSet()) {
            assetDefaultListModel.addElement(asset);
        }

    }

    /**
     * Saves the data in the database using a persistence
     * mechanism.
     */
    public void persist() {
        assetDataSource.close();
    }

    /**
     * Retrieves Asset details from the model.
     *
     * @param key the id to retrieve.
     * @return the Asset object related to the id.
     */
    public Asset get(Object key) throws Exception {
        return assetDataSource.getAsset((Integer) key);
    }

    /**
     * Accessor for the list model.
     *
     * @return the listModel to display.
     */
    public ListModel<Asset> getAssetModel() { return  assetDefaultListModel; }

    /**
     * Adds a Asset to the database.
     *
     * @param asset Asset to add to the database.
     */
    public void addAsset(Asset asset) throws Exception {
        assetDataSource.addAsset(asset);
    }

    public Boolean nameAvailability(String name) throws Exception {
        return assetDataSource.checkName(name);
    }

    public void updateAssetName(Integer assetID, String name) throws Exception {
        assetDataSource.updateAssetName(assetID, name);
    }

    public void deleteAsset(Integer assetID) throws Exception {
        assetDataSource.deleteAsset(assetID);
    }
}
