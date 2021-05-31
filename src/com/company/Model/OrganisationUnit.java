package com.company.Model;

import java.io.Serializable;

public class OrganisationUnit implements Comparable<OrganisationUnit>, Serializable {

    private Integer organisationUnitID;
    private String organisationUnitName;
    private Double organisationUnitCredits;

    public OrganisationUnit() {}

    public OrganisationUnit(Integer organisationUnitID, String organisationUnitName, Double organisationUnitCredits) {
        this.organisationUnitID = organisationUnitID;
        this.organisationUnitName = organisationUnitName;
        this.organisationUnitCredits = organisationUnitCredits;
    }

    public OrganisationUnit(String organisationUnitName, Double organisationUnitCredits) {
        this.organisationUnitName = organisationUnitName;
        this.organisationUnitCredits = organisationUnitCredits;
    }

    public OrganisationUnit(Integer organisationUnitID) {
        this.organisationUnitID = organisationUnitID;
    }

    public String toString() { return (this.organisationUnitName); }

    public Integer getID() { return this.organisationUnitID; }
    public void  setID(Integer organisationUnitID) { this.organisationUnitID = organisationUnitID; }

    public String getName() { return  this.organisationUnitName; }
    public void  setName(String name) { this.organisationUnitName = name; }

    public Double getCredits() { return this.organisationUnitCredits; }
    public void  setCredits(Double organisationUnitCredits) { this.organisationUnitCredits = organisationUnitCredits ;}

    @Override
    public int compareTo(OrganisationUnit other) {
        return this.organisationUnitName.compareTo(other.getName());
    }
}
