package com.company.Database.OrganisationUnit;

import com.company.Model.OrganisationUnit;
import java.util.ArrayList;

public interface OrganisationUnitDataSource {

    /**
     * Adds a Organisation Unit to the database, if they are not already in the database
     *
     * @param organisationUnit OrganisationUnit to add
     * @throws Exception Throws exception if add fails
     */
    void addOrganisationUnit(OrganisationUnit organisationUnit) throws Exception;

    /**
     * Extracts all the details of a OrganisationUnit from the database based on the
     * OrganisationUnitID passed in.
     *
     * @param organisationUnitID The organisationUnitID as a Integer to search for.
     * @return all details in a OrganisationUnit object for the organisationUnitID
     * @throws Exception Throws exception if get fails
     */
    OrganisationUnit getOrganisationUnit(Integer organisationUnitID) throws Exception;

    /**
     * Finalizes any resources used by the data source and ensures data is
     * persisted.
     */
    void close();

    /**
     * Update OrganisationUnit
     * @param organisationUnit organisationUnit to update
     * @throws Exception Throws exception if fails
     */
    void updateOrgUnit(OrganisationUnit organisationUnit) throws Exception;

    void updateOrgUnitCredits(Integer orgUnitID, Double creditsToUpdate) throws Exception;

    /**
     * Gets list of organisation units
     * @return Arraylist of organisational units
     * @throws Exception Throws exception if get fails
     */
    ArrayList<Object[]> getList() throws Exception;


}
