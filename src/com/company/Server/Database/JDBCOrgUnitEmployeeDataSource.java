package com.company.Server.Database;

import com.company.Common.DataSource.OrgUnitEmployeeDataSource;
import com.company.Common.Model.UnitEmployee;

import java.sql.*;

public class JDBCOrgUnitEmployeeDataSource implements OrgUnitEmployeeDataSource {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `OrgUnitEmployees` (" +
            "`organisationUnitID` int(11) NOT NULL," +
            "`userID` int(11) NOT NULL," +
            "PRIMARY KEY (`organisationUnitID`,`userID`)," +
            "FOREIGN KEY (`organisationUnitID`)" +
            "REFERENCES OrganisationUnit (`organisationUnitID`)" +
            "ON DELETE CASCADE," +
            "FOREIGN KEY (`userID`)" +
            "REFERENCES Users (`userID`)" +
            "ON DELETE CASCADE);";

    private static final String INSERT_EMPLOYEE = "INSERT INTO `cab302`.`OrgUnitEmployees`" +
            "(`organisationUnitID`," +
            "`userID`)" +
            "VALUES" +
            "(?,?);";

    private static final String GET_ORG_ASSET_SET = "SELECT *" +
            "FROM `cab302`.`OrgUnitEmployees`" +
            "WHERE userID=?;";


    private Connection connection;

    private PreparedStatement addEmployee;
    private PreparedStatement getEmployee;


    public JDBCOrgUnitEmployeeDataSource() {
        connection = DBConnector.getInstance();

        try {
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE);

            addEmployee = connection.prepareStatement(INSERT_EMPLOYEE);
            getEmployee = connection.prepareStatement(GET_ORG_ASSET_SET);


        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void addEmployee(UnitEmployee unitEmployee) {
        try {
            addEmployee.setInt(1, unitEmployee.getOrgID());
            addEmployee.setInt(2, unitEmployee.getUserID());
            addEmployee.execute();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public UnitEmployee getEmployee(Integer userID) {
        ResultSet resultSet = null;

        try {
            getEmployee.setInt(1, userID);
            resultSet = getEmployee.executeQuery();

            if (resultSet.next()) {

                return new UnitEmployee(resultSet.getInt("userID"), resultSet.getInt("organisationUnitID"));

            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return new UnitEmployee();
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
