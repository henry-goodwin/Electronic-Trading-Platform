package com.company.Database.OrganisationUnit;

import com.company.Database.DBConnector;
import com.company.Model.OrganisationUnit;

import java.sql.*;
import java.util.Set;
import java.util.TreeSet;

public class JDBCOrganisationUnitDataSource implements OrganisationUnitDataSource {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `OrganisationUnit` (" +
            "  `organisationUnitID` int(11) NOT NULL AUTO_INCREMENT," +
            "  `name` varchar(255) NOT NULL," +
            "  `credits` double DEFAULT NULL," +
            "  PRIMARY KEY (`organisationUnitID`)" +
            ");";

    private static final String INSERT_ORGANISATION_UNIT = "INSERT INTO `cab302`.`OrganisationUnit`" +
            "(`name`,\n" +
            "`credits`)\n" +
            "VALUES\n" +
            "(?,?);\n";

    private static final String GET_ORGANISATION_UNITS = "SELECT * FROM `cab302`.`OrganisationUnit`;";

    private static final String GET_ORGANISATION_UNIT = "SELECT * FROM `cab302`.`OrganisationUnit` WHERE organisationUnitID=?;";

    private Connection connection;

    private PreparedStatement addOrganisationUnit;

    private PreparedStatement getOrganisationUnits;

    private PreparedStatement getOrganisationUnit;

    public JDBCOrganisationUnitDataSource() {
        connection = DBConnector.getInstance();

        try {
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE);

            addOrganisationUnit = connection.prepareStatement(INSERT_ORGANISATION_UNIT);
            getOrganisationUnit = connection.prepareStatement(GET_ORGANISATION_UNIT);
            getOrganisationUnits = connection.prepareStatement(GET_ORGANISATION_UNITS);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }


    @Override
    public void addOrganisationUnit(OrganisationUnit organisationUnit) {
        try {
            addOrganisationUnit.setString(1, organisationUnit.getName());
            addOrganisationUnit.setDouble(2, organisationUnit.getCredits());
            addOrganisationUnit.execute();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public OrganisationUnit getOrganisationUnit(Integer organisationUnitID) {
        ResultSet resultSet = null;

        try {
            getOrganisationUnit.setInt(1, organisationUnitID);
            resultSet = getOrganisationUnit.executeQuery();

            if (resultSet.next()) {

                return new OrganisationUnit(resultSet.getInt("organisationUnitID"), resultSet.getString("name"), resultSet.getDouble("credits"));

            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Set<OrganisationUnit> organisationUnitSet() {
        Set<OrganisationUnit> organisationUnits = new TreeSet<OrganisationUnit>();
        ResultSet resultSet = null;

        try {
            resultSet = getOrganisationUnits.executeQuery();

            while (resultSet.next()) {
                OrganisationUnit organisationUnit = new OrganisationUnit(resultSet.getInt("organisationUnitID"), resultSet.getString("name"), resultSet.getDouble("credits"));
                organisationUnits.add(organisationUnit);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return organisationUnits;
    }
}
