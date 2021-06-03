package com.company.Database.OrganisationUnit;

import com.company.Model.OrganisationUnit;

import javax.swing.*;
import java.util.ArrayList;

public class OrganisationUnitData {

    private DefaultListModel<OrganisationUnit> organisationUnitDefaultListModel;
    private OrganisationUnitDataSource organisationUnitDataSource;

    public OrganisationUnitData(OrganisationUnitDataSource dataSource) {
        organisationUnitDataSource = dataSource;
        organisationUnitDefaultListModel = new DefaultListModel<OrganisationUnit>();
    }

    /**
     * Saves the data in the database using a persistence
     * mechanism.
     */
    public void persist() {
        organisationUnitDataSource.close();
    }

    /**
     * Retrieves OrganisationUnit details from the model.
     *
     * @param key the id to retrieve.
     * @return the OrganisationUnit object related to the id.
     * @throws Exception throws exception if get fails
     */
    public OrganisationUnit get(Object key) throws Exception {
        try {
            return organisationUnitDataSource.getOrganisationUnit((Integer) key);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to get unit");
        }
    }

    /**
     * Adds a OrganisationUnit to the database.
     *
     * @param organisationUnit OrganisationUnit to add to the database.
     * @throws Exception throws exception if add fails
     */
    public void addOrganisationUnit(OrganisationUnit organisationUnit) throws Exception {
        try {
            organisationUnitDataSource.addOrganisationUnit(organisationUnit);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to add organisational unit");
        }
    }

    public void updateUnit(OrganisationUnit organisationUnit) throws Exception {
        try {
            organisationUnitDataSource.updateOrgUnit(organisationUnit);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Update failed");
        }
    }

    public ArrayList<Object[]> getList() throws Exception {
        try {
            return organisationUnitDataSource.getList();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to get list");
        }
    }

}
