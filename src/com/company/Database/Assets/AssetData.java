package com.company.Database.Assets;

import com.company.Database.OrganisationUnit.OrganisationUnitDataSource;
import com.company.Model.Asset;
import com.company.Model.OrganisationUnit;

import javax.swing.*;

public class AssetData {

    private DefaultListModel<Asset> assetDefaultListModel;
    private AssetDataSource assetDataSource;

    public AssetData(AssetDataSource dataSource) {
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
    public Asset get(Object key) {
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
    public void addAsset(Asset asset) {
        assetDataSource.addAsset(asset);
    }

    public Boolean nameAvailability(String name) {
        return assetDataSource.checkName(name);
    }
}
