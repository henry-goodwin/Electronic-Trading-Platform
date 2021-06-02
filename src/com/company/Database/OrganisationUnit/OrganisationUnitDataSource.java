package com.company.Database.OrganisationUnit;

import com.company.Model.OrganisationUnit;
import com.company.Model.Person;

import java.util.ArrayList;
import java.util.Set;

public interface OrganisationUnitDataSource {

    /**
     * Adds a Organisation Unit to the database, if they are not already in the database
     *
     * @param organisationUnit OrganisationUnit to add
     */
    void addOrganisationUnit(OrganisationUnit organisationUnit);

    /**
     * Extracts all the details of a OrganisationUnit from the database based on the
     * OrganisationUnitID passed in.
     *
     * @param organisationUnitID The organisationUnitID as a Integer to search for.
     * @return all details in a OrganisationUnit object for the organisationUnitID
     */
    OrganisationUnit getOrganisationUnit(Integer organisationUnitID);

    /**
     * Finalizes any resources used by the data source and ensures data is
     * persisited.
     */
    void close();

    /**
     * Retrieves a set of OrganisationUnits from the data source that are used in
     * the database.
     *
     * @return set of OrganisationUnits.
     */
    Set<OrganisationUnit> organisationUnitSet();

    void updateOrgUnit(OrganisationUnit organisationUnit);

    ArrayList<Object[]> getList();


}
