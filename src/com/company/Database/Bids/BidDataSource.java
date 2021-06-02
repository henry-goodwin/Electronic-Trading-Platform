package com.company.Database.Bids;

import com.company.Model.Bid;

public interface BidDataSource {

    void addBid(Bid bid);

    Bid getBid(Integer bidID);

    void close();

}
