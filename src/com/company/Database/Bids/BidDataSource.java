package com.company.Database.Bids;

import com.company.Model.Bid;

import java.util.ArrayList;

public interface BidDataSource {

    void addBid(Bid bid) throws Exception;

    Bid getBid(Integer bidID) throws Exception;

    void close();

    ArrayList<Object[]> getBidList(Integer orgID, boolean buyType) throws Exception;

    ArrayList<Bid> getHistoryList(Integer assetID) throws Exception;

    void checkTrades() throws Exception;

    void updateBid(Integer bidID, Double activeQuantity, Double inactiveQuantity ,Double purchaseAmount) throws Exception;

}
