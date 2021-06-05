package com.company.Common.DataSource;

import com.company.Common.Model.Bid;

import java.util.ArrayList;

public interface BidDataSource {

    /**
     * Adds bid
     * @param bid
     * @throws Exception
     */
    void addBid(Bid bid) throws Exception;

    /**
     * Gets bid
     * @param bidID
     * @return
     * @throws Exception
     */
    Bid getBid(Integer bidID) throws Exception;

    /**
     * Closes connection
     */
    void close();

    /**
     * Gets list of all active bids for organisation
     * @param orgID
     * @param buyType
     * @return
     * @throws Exception
     */
    ArrayList<Object[]> getBidList(Integer orgID, boolean buyType) throws Exception;

    /**
     * Gets list of all previous sales for an asset
     * @param assetID
     * @return
     * @throws Exception
     */
    ArrayList<Bid> getHistoryList(Integer assetID) throws Exception;

    /**
     * Checks and processes bids
     * @throws Exception
     */
    void checkTrades() throws Exception;

    /**
     * Updates bid once bid is processed
     * @param bidID
     * @param activeQuantity
     * @param inactiveQuantity
     * @param purchaseAmount
     * @throws Exception
     */
    void updateBid(Integer bidID, Double activeQuantity, Double inactiveQuantity ,Double purchaseAmount) throws Exception;

}
