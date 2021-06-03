package com.company.Testing.OrgUnitTest;

import com.company.Database.OrganisationUnit.OrganisationUnitDataSource;
import com.company.Model.OrganisationUnit;

import java.lang.reflect.Array;
import java.util.*;

public class MockOrgUnitDatabase implements OrganisationUnitDataSource {

    TreeMap<Integer, OrganisationUnit> data;

    /**
     * Initializer function
     */
    public MockOrgUnitDatabase() {
        data = new TreeMap<Integer, OrganisationUnit>();
        try {
            OrganisationUnit organisationUnit1 = new OrganisationUnit();
            organisationUnit1.setID(1);
            organisationUnit1.setName("Accounting");
            organisationUnit1.setCredits(200.00);

            OrganisationUnit organisationUnit2 = new OrganisationUnit();
            organisationUnit2.setID(2);
            organisationUnit2.setName("Service");
            organisationUnit2.setCredits(50.0);

            OrganisationUnit organisationUnit3 = new OrganisationUnit();
            organisationUnit3.setID(3);
            organisationUnit3.setName("Finance");
            organisationUnit3.setCredits(700.0);

            data.put(1, organisationUnit1);
            data.put(2, organisationUnit2);
            data.put(3, organisationUnit3);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Function to add organisation unit to database
     * @param organisationUnit OrganisationUnit to add
     * @throws Exception throws exception if Organisational Unit is invalid
     */
    @Override
    public void addOrganisationUnit(OrganisationUnit organisationUnit) throws Exception {
        if(organisationUnit.getName() != null && organisationUnit.getCredits() != null) {
            data.put(organisationUnit.getID(), organisationUnit);
        } else {
            throw new Exception("Error: Invalid Org ID");
        }
    }

    /**
     * Function to get organisation unit from organisationUnitID
     * @param organisationUnitID The organisationUnitID as a Integer to search for.
     * @return organisationUnit from the given organisationUnitID
     * @throws Exception throws exception if get fails
     */
    @Override
    public OrganisationUnit getOrganisationUnit(Integer organisationUnitID) throws Exception {
        if (organisationUnitID != null) {
            if (data.get(organisationUnitID).equals(null)) {
                throw new Exception("Error: no org unit found");
            } else {
                return data.get(organisationUnitID);
            }
        } else {
            throw new Exception("Error: Invalid Org Unit ID");
        }
    }

    /**
     * Close database connection
     */
    @Override
    public void close() { }


    /**
     *  Update organisationUnit
     * @param organisationUnit organisationUnit to update
     * @throws Exception throws exception if org unit is invalid
     */
    @Override
    public void updateOrgUnit(OrganisationUnit organisationUnit) throws Exception {
        if (organisationUnit.getName().equals(null) || organisationUnit.getID() == null || organisationUnit.getCredits() == null) {

            throw new Exception("Invalid Org Unit");

        } else {
            data.get(organisationUnit.getID()).setID(organisationUnit.getID());
            data.get(organisationUnit.getID()).setName(organisationUnit.getName());
            data.get(organisationUnit.getID()).setCredits(organisationUnit.getCredits());
        }
    }

    /**
     * Gets array list of all organisation units, used for JLists
     * @return arrayList of all organisational units
     */
    @Override
    public ArrayList<Object[]> getList() {
        ArrayList<Object[]> assetList = new ArrayList<>();

        for(Map.Entry<Integer,OrganisationUnit> entry : data.entrySet()) {
            Integer key = entry.getKey();
            OrganisationUnit value = entry.getValue();

            Object[] temp = new Object[] {value, value.getCredits()};
            assetList.add(temp);
        }

        return assetList;
    }
}
