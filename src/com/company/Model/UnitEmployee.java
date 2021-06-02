package com.company.Model;

import java.io.Serializable;

public class UnitEmployee implements Comparable<UnitEmployee>, Serializable {

    private Integer userID;
    private Integer orgID;

    public UnitEmployee() {}

    public UnitEmployee(Integer userID, Integer orgID) {
        this.userID = userID;
        this.orgID = orgID;
    }

    public Integer getOrgID() { return orgID; }
    public Integer getUserID() {return userID; }

    @Override
    public int compareTo(UnitEmployee o) {
        return userID.compareTo(o.userID);
    }
}
