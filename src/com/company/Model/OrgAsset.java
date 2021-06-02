package com.company.Model;

import com.company.Database.Assets.AssetData;
import com.company.Database.OrganisationUnit.OrganisationUnitData;
import com.company.Database.OrganisationUnit.OrganisationUnitDataSource;
import com.company.NetworkDataSource.AssetNDS;
import com.company.NetworkDataSource.OrganisationUnitNDS;

import java.io.Serializable;

public class OrgAsset implements Comparable<OrgAsset>, Serializable {

    private Integer organisationUnitID;
    private Integer assetID;
    private Double quantity;

    public OrgAsset(){}

    public OrgAsset(Integer organisationUnitID, Integer assetID, Double quantity) {
        this.organisationUnitID = organisationUnitID;
        this.assetID = assetID;
        this.quantity = quantity;
    }

    public String toString() {

        AssetData assetData = new AssetData(new AssetNDS());
        String assetName = assetData.get(assetID).getName();

        return assetName;
    }

    public Integer getOrganisationUnitID() {return organisationUnitID;}

    public OrganisationUnit getOrganisationUnit() {
        // Create Connection to database & get OrganisationUnit
        OrganisationUnitData organisationUnitData = new OrganisationUnitData(new OrganisationUnitNDS());
        return organisationUnitData.get(organisationUnitID);
    }

    public Integer getAssetID() { return assetID; }

    public Asset getAsset() {
        // Create Connection to database & get Asset
        AssetData assetData = new AssetData(new AssetNDS());
        return assetData.get(assetID);
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }
    public void addQuantity(Double quantity) {this.quantity += quantity;}
    public void removeQuantity(Double quantity) {this.quantity -= quantity;}


    public Double getQuantity() {
        return this.quantity;
    }

    @Override
    public int compareTo(OrgAsset o) {
        return 0;
    }
}