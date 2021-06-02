package com.company.Database.Bids;

import com.company.Model.Bid;

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

    public Bid get(Integer bidID) { return bidDataSource.getBid(bidID); }

    public void addBid(Bid bid) {bidDataSource.addBid(bid);}

}
