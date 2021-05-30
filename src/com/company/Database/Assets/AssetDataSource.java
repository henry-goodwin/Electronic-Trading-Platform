package com.company.Database.Assets;

import com.company.Model.Asset;
import com.company.Model.OrganisationUnit;

import java.util.Set;

public interface AssetDataSource {
    /**
     * Adds a Asset to the database, if they are not already in the database
     *
     * @param asset Asset to add
     */
    void addAsset(Asset asset);

    /**
     * Extracts all the details of a Asset from the database based on the
     * Asset ID passed in.
     *
     * @param assetID The assetID as a Integer to search for.
     * @return all details in a Asset object for the assetID
     */
    Asset getAsset(Integer assetID);


    Boolean checkName(String string);


    /**
     * Finalizes any resources used by the data source and ensures data is
     * persisited.
     */


    void close();

    /**
     * Retrieves a set of Assets from the data source that are used in
     * the database.
     *
     * @return set of Assets.
     */
    Set<Asset> assetSet();

    void updateAssetName(Integer assetID, String name);

    void deleteAsset(Integer assetID);
}
