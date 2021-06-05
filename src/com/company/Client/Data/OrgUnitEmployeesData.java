package com.company.Client.Data;

import com.company.Common.DataSource.OrgUnitEmployeeDataSource;
import com.company.Common.Model.UnitEmployee;

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
