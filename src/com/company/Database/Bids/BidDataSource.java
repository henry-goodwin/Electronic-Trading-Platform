package com.company.Database.Bids;

import com.company.Model.Bid;

import java.util.ArrayList;

public interface BidDataSource {

    void addBid(Bid bid);

    Bid getBid(Integer bidID);

    void close();

    ArrayList<Object[]> getBidList(Integer orgID, boolean buyType);

    void checkTrades();

}
