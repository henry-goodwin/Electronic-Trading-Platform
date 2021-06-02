package com.company.Database.OrgUnitEmployees;

import com.company.Model.UnitEmployee;

public interface OrgUnitEmployeeDataSource {

    void addEmployee(UnitEmployee unitEmployee);

    UnitEmployee getEmployee(Integer userID);

    void close();



}
