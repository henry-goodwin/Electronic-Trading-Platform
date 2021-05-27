package com.company.Model;

import java.io.Serializable;

public class Asset implements Comparable<Asset>, Serializable {

    private Integer assetID;
    private String name;

    public Asset() {}

    public Asset(Integer assetID, String name) {
        this.assetID = assetID;
        this.name = name;
    }

    public Asset(Integer assetID) {
        this.assetID = assetID;
    }

    public Asset(String name) {
        this.name = name;
    }

    public String toString() { return (this.name); }

    public void setName(String name) {
        this.name = name;
    }

    public void setAssetID(Integer assetID) {
        this.assetID = assetID;
    }

    public Integer getAssetID() {
        return this.assetID;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public int compareTo(Asset other) {
        return this.name.compareTo(other.getName());
    }
}
