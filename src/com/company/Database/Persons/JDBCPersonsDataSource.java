package com.company.Database.Persons;

import com.company.Database.DBConnector;
import com.company.Database.Users.JDBCUsersDataSource;
import com.company.Model.Person;
import com.company.Model.User;

import java.sql.*;
import java.util.Set;
import java.util.TreeSet;

public class JDBCPersonsDataSource implements PersonsDataSource {

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `Person` (\n" +
            "  `personID` int(11) NOT NULL AUTO_INCREMENT,\n" +
            "  `firstName` varchar(45) NOT NULL,\n" +
            "  `lastName` varchar(45) NOT NULL,\n" +
            "  PRIMARY KEY (`personID`)\n" +
            ");\n";

    private static final String INSERT_PERSON = "INSERT INTO `cab302`.`Person`\n" +
            "(`firstName`,\n" +
            "`lastName`)\n" +
            "VALUES\n" +
            "(?,\n" +
            "?);";

    private static final String GET_PERSONID = "SELECT `Person`.`personID` FROM `cab302`.`Person`;";

    private static final String GET_PERSON = "SELECT * FROM `cab302`.`Person` WHERE personID=?;";

    private Connection connection;

    private PreparedStatement addPerson;

    private PreparedStatement getPersonList;

    private PreparedStatement getPerson;

    public JDBCPersonsDataSource() {
        connection = DBConnector.getInstance();

        try {
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE);
            /* BEGIN MISSING CODE */
            addPerson = connection.prepareStatement(INSERT_PERSON);
            getPersonList = connection.prepareStatement(GET_PERSONID);
            getPerson = connection.prepareStatement(GET_PERSON);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void addPerson(Person person) {
        try {
            addPerson.setString(1, person.getFirstname());
            addPerson.setString(2, person.getLastname());

            addPerson.execute();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public Person getPerson(Integer personID) {
        Person person = new Person();
        ResultSet resultSet = null;

        try {

            getPerson.setInt(1,personID);
            resultSet = getPerson.executeQuery();
            resultSet.next();

            person.setPersonID(resultSet.getInt("personID"));
            person.setFirstname(resultSet.getString("firstName"));
            person.setLastname(resultSet.getString("lastName"));

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return person;
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
    public Set<Integer> personIDSet() {
        Set<Integer> personIDs = new TreeSet<Integer>();
        ResultSet resultSet = null;

        try {
            resultSet = getPersonList.executeQuery();

            while (resultSet.next()) {
                personIDs.add(resultSet.getInt("personID"));
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return personIDs;
    }
}
