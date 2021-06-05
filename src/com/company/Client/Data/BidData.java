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

    /**
     * Contacts the server to add a bid
     * @param bid bid to add
     * @throws Exception
     */
    public void addBid(Bid bid) throws Exception {bidDataSource.addBid(bid);}

    /**
     * Contacts the server to get a list of all buy or sell bids for an organisation
     * @param orgID orgID to search for
     * @param buyType buy type
     * @return
     * @throws Exception
     */
    public ArrayList<Object[]> getBidList(Integer orgID, Boolean buyType) throws Exception {
        return bidDataSource.getBidList(orgID, buyType);
    }

    /**
     * contacts the server to check & process all current bids, matches all active buy orders with sell order & processes the sale/purchase
     * @throws Exception
     */
    public void checkTrades() throws Exception { bidDataSource.checkTrades();}

    /**
     * contacts the server to update a bids status and inactive quantity once a sale/purchase has been processed
     * @param bidID
     * @param activeQuantity
     * @param inactiveQuantity
     * @param purchaseAmount
     * @throws Exception
     */
    public void updateBid(Integer bidID, Double activeQuantity, Double inactiveQuantity, Double purchaseAmount) throws Exception {
        bidDataSource.updateBid(bidID, activeQuantity, inactiveQuantity, purchaseAmount);
    }

    /**
     * contacts the server to return a list of all closed bids, for an assetID
     * @param assetID
     * @return
     * @throws Exception
     */
    public ArrayList<Bid> getBidHistory(Integer assetID) throws Exception {
        return bidDataSource.getHistoryList(assetID);
    }
}
