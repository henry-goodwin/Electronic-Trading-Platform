package com.company.Model;

import java.io.Serializable;

public class Asset implements Comparable<Asset>, Serializable {

    // Variables
    private Integer assetID;
    private String name;

    /**
     * Constructor for asset
     */
    public Asset() {}

    /**
     * Initialise Asset
     * @param assetID assetID to set
     * @param name name to set
     * @throws Exception Throws exception if fails
     */
    public Asset(Integer assetID, String name) throws Exception {
        setAssetID(assetID);
        setName(name);
    }

    /**
     * Initialize asset
     * @param assetID assetID to set
     * @throws Exception throws exception if set fails
     */
    public Asset(Integer assetID) throws Exception {
        setAssetID(assetID);
    }

    /**
     * Initialize asset
     * @param name name to set
     * @throws Exception throws exception if set fails
     */
    public Asset(String name) throws Exception {
        setName(name);
    }

    /**
     * Gets name of asset
     * @return name of asset
     */
    public String toString() { return (this.name); }

    /**
     * Set name of asset
     * @param name name to set
     * @throws Exception throw exception if name is empty
     */
    public void setName(String name) throws Exception {
        if (name.equals("")) throw new Exception("Invalid Name");
        this.name = name;
    }

    /**
     * Set asset ID
     * @param assetID assetID to set
     * @throws Exception throws exception if asset ID is invalid
     */
    public void setAssetID(Integer assetID) throws Exception {
        if (assetID < 0) throw new Exception("Invalid asset ID");
        this.assetID = assetID;
    }

    /**
     * Get the asset ID for asset
     * @return assetID for asset
     */
    public Integer getAssetID() {
        return this.assetID;
    }

    /**
     * Get the name of the asset
     * @return name for the asset
     */
    public String getName() {
        return this.name;
    }

    @Override
    public int compareTo(Asset other) {
        return this.name.compareTo(other.getName());
    }
}
