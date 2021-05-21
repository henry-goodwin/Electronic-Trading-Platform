package com.company.Database.OrganisationUnit;

import com.company.Model.OrganisationUnit;
import com.company.Model.Person;

import javax.swing.*;

public class OrganisationUnitData {

    private DefaultListModel<OrganisationUnit> organisationUnitDefaultListModel;
    private OrganisationUnitDataSource organisationUnitDataSource;

    public OrganisationUnitData() {
        organisationUnitDataSource = new JDBCOrganisationUnitDataSource();
        organisationUnitDefaultListModel = new DefaultListModel<OrganisationUnit>();

        for (OrganisationUnit organisationUnit : organisationUnitDataSource.organisationUnitSet()) {
            organisationUnitDefaultListModel.addElement(organisationUnit);
        }

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
     */
    public OrganisationUnit get(Object key) {
        return organisationUnitDataSource.getOrganisationUnit((Integer) key);
    }

    /**
     * Accessor for the list model.
     *
     * @return the listModel to display.
     */
    public ListModel<OrganisationUnit> getOrganisationUnitModel() { return  organisationUnitDefaultListModel; }

    /**
     * Adds a OrganisationUnit to the database.
     *
     * @param organisationUnit OrganisationUnit to add to the database.
     */
    public void addOrganisationUnit(OrganisationUnit organisationUnit) {
        organisationUnitDataSource.addOrganisationUnit(organisationUnit);
    }



}
