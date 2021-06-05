package com.company.Common.DataSource;

import com.company.Common.Model.UnitEmployee;

public interface OrgUnitEmployeeDataSource {

    void addEmployee(UnitEmployee unitEmployee);

    UnitEmployee getEmployee(Integer userID);

    void close();



}
