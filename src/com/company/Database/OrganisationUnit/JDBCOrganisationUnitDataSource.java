package com.company.Database.OrganisationUnit;

import com.company.Database.DBConnector;
import com.company.Model.OrgAsset;
import com.company.Model.OrganisationUnit;

import java.sql.*;
import java.util.ArrayList;
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
            "(`name`," +
            "`credits`)" +
            "VALUES" +
            "(?,?);";

    private static final String GET_ORGANISATION_UNITS = "SELECT * FROM `cab302`.`OrganisationUnit`;";

    private static final String GET_ORGANISATION_UNIT = "SELECT * FROM `cab302`.`OrganisationUnit` WHERE organisationUnitID=?;";

    private static final String UPDATE_ORGANISATION_UNIT = "UPDATE `cab302`.`OrganisationUnit`" +
            "SET `credits` = ?" +
            "WHERE `organisationUnitID` = ?;";

    private static final String GET_LIST = "SELECT *" +
            "FROM `cab302`.`OrganisationUnit`;";

    private Connection connection;

    private PreparedStatement addOrganisationUnit;
    private PreparedStatement getOrganisationUnits;
    private PreparedStatement getOrganisationUnit;
    private PreparedStatement updateOrganisationUnit;
    private PreparedStatement getList;

    public JDBCOrganisationUnitDataSource() {
        connection = DBConnector.getInstance();

        try {
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE);

            addOrganisationUnit = connection.prepareStatement(INSERT_ORGANISATION_UNIT);
            getOrganisationUnit = connection.prepareStatement(GET_ORGANISATION_UNIT);
            getOrganisationUnits = connection.prepareStatement(GET_ORGANISATION_UNITS);
            updateOrganisationUnit = connection.prepareStatement(UPDATE_ORGANISATION_UNIT);
            getList = connection.prepareStatement(GET_LIST);

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

    @Override
    public void updateOrgUnit(OrganisationUnit organisationUnit) {
        try {
            updateOrganisationUnit.setDouble(1, organisationUnit.getCredits());
            updateOrganisationUnit.setInt(2, organisationUnit.getID());
            updateOrganisationUnit.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public ArrayList<Object[]> getList() {
        ArrayList<Object[]> assetList = new ArrayList<>();
        ResultSet resultSet = null;

        try {

            resultSet = getList.executeQuery();

            while (resultSet.next()) {
                // Find Asset
                OrganisationUnit organisationUnit = new OrganisationUnit(resultSet.getInt("organisationUnitID"), resultSet.getString("name"), resultSet.getDouble("credits"));
                Object[] temp = new Object[] {organisationUnit, organisationUnit.getCredits()};
                assetList.add(temp);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return assetList;
    }
}
