package com.company.Common.DataSource;

import com.company.Common.Model.OrgAsset;

import java.util.ArrayList;
import java.util.Set;

public interface OrgUnitAssetDataSource {
    /**
     * Adds a Asset to the organisation, if they are not already in the database
     *
     * @param orgAsset orgAsset to add
     *
     */
    void addAsset(OrgAsset orgAsset) throws Exception;

    /**
     * Extracts all the details of a Asset from the database based on the
     * Asset ID passed in.
     *
     * @return all details in a OrgAsset object for the orgAssetID
     */
    OrgAsset getOrgAsset(Integer orgID, Integer assetID) throws Exception;

    /**
     * Finalizes any resources used by the data source and ensures data is
     * persisited.
     */


    void close();

    /**
     * Retrieves a set of OrgAsset from the data source that are used in
     * the database.
     *
     * @return set of OrgAsset.
     */
    Set<OrgAsset> OrgAssetSet() throws Exception;

    /**
     * Retrieves a set of OrgAsset from the data source that are used in
     * the organisation.
     *
     * @return set of OrgAsset.
     */
    Set<OrgAsset> myOrgAssetSet(Integer orgID) throws Exception;

    Boolean checkAsset(Integer orgID, Integer assetID) throws Exception;

    ArrayList<Object[]> getAssetList(Integer orgID) throws Exception;

    void updateQuantity(Integer orgID, Integer assetID ,Double quantity) throws Exception;

    void updateOrgAsset(OrgAsset orgAsset) throws Exception;

    void updateData(Integer orgUnitID, Integer assetID, Double quantity) throws Exception;
}
