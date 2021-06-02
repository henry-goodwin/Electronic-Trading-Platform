package com.company.Database.OrgUnitEmployees;

import com.company.Database.OrgUnitAssets.OrgUnitAssetDataSource;
import com.company.Model.OrgAsset;
import com.company.Model.UnitEmployee;

import javax.swing.*;
import java.util.ArrayList;

public class OrgUnitEmployeesData {

    private OrgUnitEmployeeDataSource orgUnitEmployeeDataSource;


    public OrgUnitEmployeesData(OrgUnitEmployeeDataSource dataSource) {
        orgUnitEmployeeDataSource = dataSource;
    }

    /**
     * Saves the data in the database using a persistence
     * mechanism.
     */
    public void persist() {
        orgUnitEmployeeDataSource.close();
    }

    /**
     * Retrieves OrgAsset details from the model.
     *
     * @return the OrgAsset object related to the id.
     */
    public UnitEmployee get(Integer userID) {
        return orgUnitEmployeeDataSource.getEmployee(userID);
    }

    /**
     * Adds a OrgAsset to the database.
     *
     */
    public void addEmployee(UnitEmployee unitEmployee) {
        orgUnitEmployeeDataSource.addEmployee(unitEmployee);
    }

}
