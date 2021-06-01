package com.company.Model;

import java.io.Serializable;
import java.sql.Date;

public class Bid implements Comparable<Bid>, Serializable {

    private Integer bidID;
    private Integer assetID;
    private Integer orgID;
    private String status;
    private Boolean buyType;
    private Double price;
    private Double quantity;
    private java.sql.Date date;

    public Bid(Integer bidID, Integer assetID, Integer orgID, String status, Boolean buyType, Double price, Double quantity, java.sql.Date date) {
        this.bidID = bidID;
        this.assetID = assetID;
        this.orgID = orgID;
        this.status = status;
        this.buyType = buyType;
        this.price = price;
        this.quantity = quantity;
        this.date = date;
    }

    public Bid(Integer assetID, Integer orgID, String status, Boolean buyType, Double price, Double quantity) {
        this.assetID = assetID;
        this.orgID = orgID;
        this.status = status;
        this.buyType = buyType;
        this.price = price;
        this.quantity = quantity;
    }

    public Bid() {

    }

    public void setBidID(Integer bidID) {this.bidID = bidID;}
    public void setAssetID(Integer assetID) {this.assetID = assetID;}
    public void setOrgID(Integer orgID) {this.orgID = orgID;}
    public void setStatus(String status) {this.status = status;}
    public void setBuyType(Boolean buyType) {this.buyType = buyType;}
    public void setQuantity(Double quantity) {this.quantity = quantity;}
    public void setPrice(Double price) {this.price = price;}
    public void setDate(Date date) {this.date = date;}

    public Integer getBidID() { return bidID; }
    public Integer getAssetID() {return assetID;}
    public Integer getOrgID() {return orgID;}
    public String getStatus() { return status; }
    public Boolean getBuyType() { return  buyType; }
    public Double getPrice() { return  price; }
    public Double getQuantity() { return  quantity;}
    public java.sql.Date getDate() { return date; }

    @Override
    public int compareTo(Bid other) {
        return date.compareTo(other.date);
    }
}
