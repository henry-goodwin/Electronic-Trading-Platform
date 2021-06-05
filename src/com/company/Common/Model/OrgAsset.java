package com.company.Common.Model;

import com.company.Client.Data.AssetData;
import com.company.Client.Data.OrganisationUnitData;
import com.company.Common.NetworkDataSource.AssetNDS;
import com.company.Common.NetworkDataSource.OrganisationUnitNDS;

import java.io.Serializable;

public class OrgAsset implements Comparable<OrgAsset>, Serializable {

    private Integer organisationUnitID;
    private Integer assetID;
    private Double quantity;

    public OrgAsset(){}

    public OrgAsset(Integer organisationUnitID, Integer assetID, Double quantity) throws Exception {
        setOrgID(organisationUnitID);
        setAssetID(assetID);
        setQuantity(quantity);
    }

    public String toString() {
        AssetData assetData = null;
        try {
            assetData = new AssetData(new AssetNDS());
            String assetName = assetData.get(assetID).getName();
            return assetName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public Integer getOrganisationUnitID() {return organisationUnitID;}

    public OrganisationUnit getOrganisationUnit() throws Exception {
        // Create Connection to database & get OrganisationUnit
        OrganisationUnitData organisationUnitData = new OrganisationUnitData(new OrganisationUnitNDS());
        return organisationUnitData.get(organisationUnitID);
    }

    public void setOrgID(Integer orgID) throws Exception {
        if (orgID < 0) throw new Exception("Error: Org ID is less than 0");
        this.organisationUnitID = orgID;
    }

    public Integer getAssetID() { return assetID; }

    public void setAssetID(Integer assetID) throws Exception {
        if (assetID < 0) throw new Exception("Error: Asset ID is less than 0");
        this.assetID = assetID;
    }

    public Asset getAsset() throws Exception {
        // Create Connection to database & get Asset
        AssetData assetData = new AssetData(new AssetNDS());
        return assetData.get(assetID);
    }

    public void setQuantity(Double quantity) throws Exception {
        if (quantity < 0) throw new Exception("Quantity is less than 0");
        this.quantity = quantity;

    }
    public void addQuantity(Double quantity) throws Exception {
        if (quantity < 0) throw new Exception("Error Quantity is less than 0");
        if (this.quantity == null) throw new Exception("Error: Org Asset Quantity is null");
        this.quantity += quantity;
    }

    public void removeQuantity(Double quantity) throws Exception {
        if (quantity < 0) throw new Exception("Error Quantity is less than 0");
        if (this.quantity == null) throw new Exception("Error: Org Asset Quantity is null");

        this.quantity -= quantity;
    }


    public Double getQuantity() {
        return this.quantity;
    }

    @Override
    public int compareTo(OrgAsset o) {
        return 0;
    }
}