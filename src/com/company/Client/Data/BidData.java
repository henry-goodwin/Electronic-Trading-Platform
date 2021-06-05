package com.company.Client.Data;

import com.company.Common.DataSource.BidDataSource;
import com.company.Common.Model.Bid;

import java.util.ArrayList;

public class BidData {

    private BidDataSource bidDataSource;

    public BidData(BidDataSource bidDataSource) {
        this.bidDataSource = bidDataSource;
    }

    /**
     * Saves the data in the database using a persistence
     * mechanism.
     */
    public void persist() {
        bidDataSource.close();
    }

    public Bid get(Integer bidID) throws Exception { return bidDataSource.getBid(bidID); }

    public void addBid(Bid bid) throws Exception {bidDataSource.addBid(bid);}

    public ArrayList<Object[]> getBidList(Integer orgID, Boolean buyType) throws Exception {
        return bidDataSource.getBidList(orgID, buyType);
    }

    public void checkTrades() throws Exception { bidDataSource.checkTrades();}

    public void updateBid(Integer bidID, Double activeQuantity, Double inactiveQuantity, Double purchaseAmount) throws Exception {
        bidDataSource.updateBid(bidID, activeQuantity, inactiveQuantity, purchaseAmount);
    }

    public ArrayList<Bid> getBidHistory(Integer assetID) throws Exception {
        return bidDataSource.getHistoryList(assetID);
    }
}
