package com.company.Database.Persons;

import com.company.Database.DBConnector;
import com.company.Database.Users.JDBCUsersDataSource;
import com.company.Model.Person;
import com.company.Model.User;

import java.sql.*;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class JDBCPersonsDataSource implements PersonsDataSource {

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `Persons` (\n" +
            "  `personID` int(11) NOT NULL AUTO_INCREMENT,\n" +
            "  `firstName` varchar(255) NOT NULL,\n" +
            "  `lastName` varchar(255) NOT NULL,\n" +
            "  PRIMARY KEY (`personID`)\n" +
            ");\n";

    private static final String INSERT_PERSON = "INSERT INTO `cab302`.`Persons`\n" +
            "(`firstName`,\n" +
            "`lastName`)\n" +
            "VALUES\n" +
            "(?,\n" +
            "?);";

    private static final String GET_PERSONS = "SELECT * FROM `cab302`.`Persons`;";

    private static final String GET_PERSONID = "SELECT `personID` FROM `cab302`.`Persons`;";

    private static final String GET_PERSON = "SELECT * FROM `cab302`.`Persons` WHERE personID=?;";

    private Connection connection;

    private PreparedStatement addPerson;

    private PreparedStatement getPersonList;

    private PreparedStatement getPerson;

    private PreparedStatement getPersons;

    public JDBCPersonsDataSource() {
        connection = DBConnector.getInstance();

        try {
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE);
            getPersons = connection.prepareStatement(GET_PERSONS);
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

            if (resultSet.next()) {

                person.setPersonID(resultSet.getInt("personID"));
                person.setFirstname(resultSet.getString("firstName"));
                person.setLastname(resultSet.getString("lastName"));

            }

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
            // Failed
            exception.printStackTrace();
        }

        return personIDs;
    }

    @Override
    public Set<Person> personsSet() {
        Set<Person> persons = new TreeSet<Person>();
        ResultSet resultSet = null;

        try {
            resultSet = getPersons.executeQuery();

            while (resultSet.next()) {
                Person person = new Person();
                person.setPersonID(resultSet.getInt("personID"));
                person.setFirstname(resultSet.getString("firstName"));
                person.setLastname(resultSet.getString("lastName"));

                persons.add(person);
            }

        } catch (SQLException exception) {
            // Failed
            exception.printStackTrace();
        }

        return persons;
    }
}
