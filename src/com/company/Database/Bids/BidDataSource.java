package com.company.Database.Bids;

import com.company.Model.Bid;

import java.util.ArrayList;

public interface BidDataSource {

    void addBid(Bid bid) throws Exception;

    Bid getBid(Integer bidID) throws Exception;

    void close();

    ArrayList<Object[]> getBidList(Integer orgID, boolean buyType) throws Exception;

    void checkTrades() throws Exception;

}
