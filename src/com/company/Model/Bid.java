package com.company.Model;

import java.io.Serializable;
import java.sql.Date;

public class Bid implements Comparable<Bid>, Serializable {

    // Private Variables
    private Integer bidID;
    private Integer assetID;
    private Integer orgID;
    private String status;
    private Boolean buyType;
    private Double price;
    private Double activeQuantity;
    private Double inactiveQuantity;
    private java.sql.Date date;

    /**
     * Constructor for new bid
     * @param bidID bidID to set
     * @param assetID assetID to set
     * @param orgID orgID to set
     * @param status status to set
     * @param buyType buyType to set
     * @param price price to set
     * @param activeQuantity activeQuantity to set
     * @param inactiveQuantity inactiveQuantity to set
     * @param date date to set
     * @throws Exception Throw exception if set fails
     */
    public Bid(Integer bidID, Integer assetID, Integer orgID, String status, Boolean buyType, Double price, Double activeQuantity, Double inactiveQuantity,java.sql.Date date) throws Exception {
        setBidID(bidID);
        setAssetID(assetID);
        setOrgID(orgID);
        setStatus(status);
        setBuyType(buyType);
        setPrice(price);
        setActiveQuantity(activeQuantity);
        setInactiveQuantity(inactiveQuantity);
        setDate(date);
    }

    /**
     * Constructor for new bid
     * @param assetID assetID to set
     * @param orgID orgID to set
     * @param status status to set
     * @param buyType buyType to set
     * @param price price to set
     * @param activeQuantity activeQuantity to set
     * @throws Exception Throw exception if set fails
     */
    public Bid(Integer assetID, Integer orgID, String status, Boolean buyType, Double price, Double activeQuantity) throws Exception {
        setAssetID(assetID);
        setOrgID(orgID);
        setStatus(status);
        setBuyType(buyType);
        setPrice(price);
        setActiveQuantity(activeQuantity);
        this.inactiveQuantity = 0.0;
    }

    /**
     * Constructor for bid
     */
    public Bid() {

    }

    /**
     * Get Bid ID
     * @return
     */
    public String toString() {
        return String.valueOf(bidID);
    }

    /**
     * Set bidID
     * @param bidID bidID to set
     * @throws Exception throws exception if bidID is invalid
     */
    public void setBidID(Integer bidID) throws Exception {
        if (bidID < 0) throw new Exception("Bid ID is negative");
        this.bidID = bidID;
    }

    /**
     * Set assetID
     * @param assetID assetID to set
     * @throws Exception Throws exception if assetID is negative
     */
    public void setAssetID(Integer assetID) throws Exception {
        if (assetID < 0) throw new Exception("Asset ID is negative");
        this.assetID = assetID;
    }

    /**
     * Set orgID
     * @param orgID orgID to set
     * @throws Exception Throws exception if orgID is invalid
     */
    public void setOrgID(Integer orgID) throws Exception {
        if (orgID < 0) throw new Exception("OrgID is negative");
        this.orgID = orgID;
    }

    /**
     * Set status
     * @param status status to set ("open", "closed", "cancelled")
     * @throws Exception Throws exception if invalid status is set
     */
    public void setStatus(String status) throws Exception {
        if (status.equals("open") || status.equals("closed") || status.equals("cancelled")) {
            this.status = status;
        } else {
            throw new Exception("Invalid status");
        }
    }

    /**
     * Set buyType
     * @param buyType true if bid is a buy order, false if bid is a sell order
     */
    public void setBuyType(Boolean buyType) {this.buyType = buyType;}


    /**
     * Set Active Quantity
     * @param quantity Active Quantity to set (How many units are initially able to be sold)
     * @throws Exception Throw exception if quantity is negative
     */
    public void setActiveQuantity(Double quantity) throws Exception {
        if (quantity <= 0) throw new Exception("Error: Quantity has to be greater than 0");
        this.activeQuantity = quantity;
    }

    /**
     * Set Active Quantity
     * @param quantity Inactive Quantity to set (How many units are sold so far)
     * @throws Exception Throw exception if quantity is negative
     */
    public void setInactiveQuantity(Double quantity) throws Exception {
        if (quantity < 0) throw new Exception("Error: Negative quantity");
        this.inactiveQuantity = quantity;
    }

    /**
     * Update Bids inactive quantity
     * @param quantity Positive quantity to add to inactive quantity
     * @throws Exception Throws exception if negative quantity is entered
     */
    public void addInactiveQuantity(Double quantity) throws Exception {
        if (quantity < 0) throw new Exception("Error: quantity cannot be negative");
        this.inactiveQuantity += quantity;
    }

    /**
     * Set the bids price
     * @param price price to set
     * @throws Exception Throws exception if price entered is negative
     */
    public void setPrice(Double price) throws Exception {
        if (price < 0) throw new Exception("Error: Price cannot be negative");
        this.price = price;
    }

    /**
     * Set the Bids Date
     * @param date date to set
     */
    public void setDate(Date date) {this.date = date;}

    /**
     * Gets the bidID
     * @return bidID
     */
    public Integer getBidID() { return bidID; }

    /**
     * Gets the assetID
     * @return assetID
     */
    public Integer getAssetID() {return assetID;}

    /**
     * Gets the orgID
     * @return orgID
     */
    public Integer getOrgID() {return orgID;}

    /**
     * gets the status of the bid
     * @return status
     */
    public String getStatus() { return status; }

    /**
     * Gets the buy type
     * @return return true if Bid is a buy order, return false if bid is a sell order
     */
    public Boolean getBuyType() { return  buyType; }

    /**
     * Gets the price of bid
     * @return price
     */
    public Double getPrice() { return  price; }

    /**
     * Gets the active quantity of a bid (How much was initially able to be sold/bought)
     * @return active quantity
     */
    public Double getActiveQuantity() { return  activeQuantity;}

    /**
     * Gets the inactive quantity of a bid (How much has been sold/bought so far)
     * @return
     */

    public Double getInactiveQuantity() { return  inactiveQuantity;}

    /**
     * Gets the date of bid
     * @return date bid was posted
     */
    public java.sql.Date getDate() { return date; }

    /**
     * Compare bids by date
     * @param other other bid
     * @return return the earliest date
     */
    @Override
    public int compareTo(Bid other) {
        return date.compareTo(other.date);
    }
}
