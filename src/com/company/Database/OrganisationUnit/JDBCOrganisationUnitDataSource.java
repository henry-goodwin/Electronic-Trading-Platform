package com.company.Database.OrganisationUnit;

import com.company.Database.DBConnector;
import com.company.Model.OrganisationUnit;

import java.sql.*;
import java.util.ArrayList;

public class JDBCOrganisationUnitDataSource implements OrganisationUnitDataSource {

    // SQL Statements
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

    private static final String UPDATE_ORGANISATION_UNIT_CREDITS = "UPDATE `cab302`.`OrganisationUnit`" +
            "SET `credits` = ?" +
            "WHERE `organisationUnitID` = ?;";

    private static final String GET_LIST = "SELECT *" +
            "FROM `cab302`.`OrganisationUnit`;";

    // Database connection
    private Connection connection;

    // Prepared statements for SQL commands
    private PreparedStatement addOrganisationUnit;
    private PreparedStatement getOrganisationUnits;
    private PreparedStatement getOrganisationUnit;
    private PreparedStatement updateOrganisationUnitCredits;
    private PreparedStatement getList;

    /**
     * Initializer function
     */
    public JDBCOrganisationUnitDataSource() {
        // Create connection to database
        connection = DBConnector.getInstance();

        try {
            // Create the table if not exists
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE);

            // Prepare Statements
            addOrganisationUnit = connection.prepareStatement(INSERT_ORGANISATION_UNIT);
            getOrganisationUnit = connection.prepareStatement(GET_ORGANISATION_UNIT);
            getOrganisationUnits = connection.prepareStatement(GET_ORGANISATION_UNITS);
            updateOrganisationUnitCredits = connection.prepareStatement(UPDATE_ORGANISATION_UNIT_CREDITS);
            getList = connection.prepareStatement(GET_LIST);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }


    /**
     * Function to add an organisational unit to the database
     * @param organisationUnit OrganisationUnit to add
     * @throws Exception throws exception if Organisational Unit is invalid
     */
    @Override
    public void addOrganisationUnit(OrganisationUnit organisationUnit) throws Exception {
        if (organisationUnit.getName() != null && organisationUnit.getCredits() != null) {
            try {
                addOrganisationUnit.setString(1, organisationUnit.getName());
                addOrganisationUnit.setDouble(2, organisationUnit.getCredits());
                addOrganisationUnit.execute();

            } catch (SQLException exception) {
                exception.printStackTrace();
                throw new Exception("SQL Error");
            }
        } else {
            throw new Exception("Invalid Org Unit");
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
        ResultSet resultSet = null;

        // Check that orgUnitID is valid
        if (organisationUnitID != null) {
            try {
                getOrganisationUnit.setInt(1, organisationUnitID);
                resultSet = getOrganisationUnit.executeQuery();

                if (resultSet.next()) {
                    OrganisationUnit organisationUnit = new OrganisationUnit();
                    organisationUnit.setID(resultSet.getInt("organisationUnitID"));
                    organisationUnit.setName(resultSet.getString("name"));
                    organisationUnit.setCredits(resultSet.getDouble("credits"));
                    return organisationUnit;
                } else {
                    throw new Exception("No organisational unit found");
                }

            } catch (SQLException exception) {
                exception.printStackTrace();
                throw new Exception("Unable to find Organisation Unit");
            }
        } else {
            throw new Exception("Invalid Org ID");
        }
    }

    /**
     * Close database connection
     */
    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Update organisationUnit, used for updating credits
     * @param organisationUnit organisationUnit to update
     */
    @Override
    public void updateOrgUnit(OrganisationUnit organisationUnit) throws Exception {
        if (organisationUnit.getName().equals(null) || organisationUnit.getID() == null || organisationUnit.getCredits() == null) {
            throw new Exception("Invalid Org Unit");
        } else {
            try {
                updateOrganisationUnitCredits.setDouble(1, organisationUnit.getCredits());
                updateOrganisationUnitCredits.setInt(2, organisationUnit.getID());
                updateOrganisationUnitCredits.executeUpdate();

            } catch (SQLException exception) {
                exception.printStackTrace();
                throw new Exception(exception.getMessage());
            }
        }
    }

    @Override
    public void updateOrgUnitCredits(Integer orgUnitID, Double creditsToUpdate) throws Exception {
        // Get Organisational Unit
        OrganisationUnit organisationUnit = getOrganisationUnit(orgUnitID);
        Double newCreditAmount = organisationUnit.getCredits() + creditsToUpdate;

        updateOrganisationUnitCredits.setDouble(1, newCreditAmount);
        updateOrganisationUnitCredits.setInt(2, orgUnitID);
        updateOrganisationUnitCredits.executeUpdate();
    }

    /**
     * Gets list of all organisational units
     * @return Array List of all organisational units
     * @throws Exception throws exception if get fails
     */
    @Override
    public ArrayList<Object[]> getList() throws Exception {
        ResultSet resultSet = null;

        try {
            ArrayList<Object[]> assetList = new ArrayList<>();
            resultSet = getList.executeQuery();

            while (resultSet.next()) {
                // Find Asset
                OrganisationUnit organisationUnit = new OrganisationUnit();
                organisationUnit.setID(resultSet.getInt("organisationUnitID"));
                organisationUnit.setName(resultSet.getString("name"));
                organisationUnit.setCredits(resultSet.getDouble("credits"));

                Object[] temp = new Object[] {organisationUnit, organisationUnit.getCredits()};
                assetList.add(temp);
            }

            return assetList;

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new Exception("SQL Error");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Class Error");
        }
    }
}
