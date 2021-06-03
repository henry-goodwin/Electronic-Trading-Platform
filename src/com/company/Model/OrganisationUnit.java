package com.company.Model;

import java.io.Serializable;

public class OrganisationUnit implements Comparable<OrganisationUnit>, Serializable {

    // Private class Variables
    private Integer organisationUnitID;
    private String organisationUnitName;
    private Double organisationUnitCredits;

    /**
     * Creates new Organisational Unit
     */
    public OrganisationUnit() {}

    /**
     * Creates new Organisational Unit with unitID, Name & Credits
     * @param organisationUnitID
     * @param organisationUnitName
     * @param organisationUnitCredits
     */
    public OrganisationUnit(Integer organisationUnitID, String organisationUnitName, Double organisationUnitCredits) {
        this.organisationUnitID = organisationUnitID;
        this.organisationUnitName = organisationUnitName;
        this.organisationUnitCredits = organisationUnitCredits;
    }

    /**
     * Creates new Organisational Unit with name & credits
     * @param organisationUnitName
     * @param organisationUnitCredits
     */
    public OrganisationUnit(String organisationUnitName, Double organisationUnitCredits) {
        this.organisationUnitName = organisationUnitName;
        this.organisationUnitCredits = organisationUnitCredits;
    }

    /**
     * Creates new Organisational Unit with credits
     * @param organisationUnitID
     */
    public OrganisationUnit(Integer organisationUnitID) {
        this.organisationUnitID = organisationUnitID;
    }

    /**
     * Gets organisational unit name that can be used for a Table or List
     * @return Organisational Unit Name
     */
    public String toString() { return (this.organisationUnitName); }

    /**
     * Gets organisational units ID
     * @return Organisational Unit ID
     */
    public Integer getID() { return this.organisationUnitID; }

    /**
     * Sets Organisational Unit ID
     * @param organisationUnitID the ID to set
     */
    public void  setID(Integer organisationUnitID) {
        this.organisationUnitID = organisationUnitID;
    }

    /**
     * Gets organisational unit name
     * @return organisational unit name
     */
    public String getName() { return  this.organisationUnitName; }

    /**
     * Sets organisational units name
     * @param name the name to set
     */
    public void  setName(String name) { this.organisationUnitName = name; }

    /**
     * Gets organisational units credits
     * @return Organisational units credits
     */
    public Double getCredits() { return this.organisationUnitCredits; }

    /**
     * Sets organisational units credits
     * @param organisationUnitCredits The credit quantity to set
     * @throws Exception throws exception if credit quantity is less than 0
     */
    public void  setCredits(Double organisationUnitCredits) throws Exception {
        if(organisationUnitCredits < 0) {
            throw new Exception("Error, please ensure credits are greater than 0");
        } else {
            this.organisationUnitCredits = organisationUnitCredits;
        }
    }

    /**
     * Adds credits to the organisation units credits, used when updating an orgUnit
     * @param credits The amount of credits to add
     * @throws Exception Throws exception if credit amount is less than 0
     */
    public void addCredits(Double credits) throws Exception {
        if (credits < 0) {
            throw new Exception("Error, credits is less than 0");
        }

        this.organisationUnitCredits += credits;
    }

    /**
     * Removes credits from the organisational units credits, used when updating an orgUnit
     * @param credits The amount of credits to remove
     */
    public void removeCredits(Double credits) throws Exception {
        if (organisationUnitCredits - credits < 0) {
            throw new Exception("Error: credits too large, returns negative number");
        }
        this.organisationUnitCredits -= credits;
    }

    /**
     * Compares organisational units names in alphabetical order
     * @param other Organisational Unit to compare
     * @return The first alphabetical organisation unit
     */
    @Override
    public int compareTo(OrganisationUnit other) {
        return this.organisationUnitName.compareTo(other.getName());
    }
}
