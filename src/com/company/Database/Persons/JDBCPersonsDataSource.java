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

    // SQL Statements
    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `Persons` (" +
            "  `personID` int(11) NOT NULL AUTO_INCREMENT," +
            "  `firstName` varchar(255) NOT NULL," +
            "  `lastName` varchar(255) NOT NULL," +
            "  PRIMARY KEY (`personID`)" +
            ");";

    private static final String INSERT_PERSON = "INSERT INTO `cab302`.`Persons`" +
            "(`firstName`," +
            "`lastName`)" +
            "VALUES" +
            "(?," +
            "?);";

    private static final String GET_PERSONS = "SELECT * FROM `cab302`.`Persons`;";

    private static final String GET_PERSONID = "SELECT `personID` FROM `cab302`.`Persons`;";

    private static final String GET_PERSON = "SELECT * FROM `cab302`.`Persons` WHERE personID=?;";

    // Database Connection
    private Connection connection;

    // Create prepared statements
    private PreparedStatement addPerson;
    private PreparedStatement getPerson;
    private PreparedStatement getPersons;

    // Initialise database
    public JDBCPersonsDataSource() {
        // Connect to database
        connection = DBConnector.getInstance();

        try {
            // Try create table
            Statement statement = connection.createStatement();
            statement.execute(CREATE_TABLE);

            // Prepare statements
            getPersons = connection.prepareStatement(GET_PERSONS);
            addPerson = connection.prepareStatement(INSERT_PERSON);
            getPerson = connection.prepareStatement(GET_PERSON);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Add person to the database
     * @param person Person to add
     * @throws Exception throws exception if
     */
    @Override
    public void addPerson(Person person) throws Exception {
        if (person.getFirstname().equals("") || person.getLastname().equals("")) throw new Exception("Invalid  name");

        try {
            addPerson.setString(1, person.getFirstname());
            addPerson.setString(2, person.getLastname());

            addPerson.execute();

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new Exception("SQL Error");
        }
    }

    /**
     * Gets a person from the database
     * @param personID The personID as a Integer to search for.
     * @return Person with the personID
     * @throws Exception throws exception personID is invalid
     */
    @Override
    public Person getPerson(Integer personID) throws Exception {

        if (personID < 0) throw new Exception("Invalid personID");

        ResultSet resultSet = null;

        try {
            Person person = new Person();
            getPerson.setInt(1,personID);
            resultSet = getPerson.executeQuery();

            if (resultSet.next()) {

                person.setPersonID(resultSet.getInt("personID"));
                person.setFirstname(resultSet.getString("firstName"));
                person.setLastname(resultSet.getString("lastName"));
                return person;

            } else {
                throw new Exception("No person found");
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
            throw new Exception("SQL Error");
        }
    }

    /**
     * Closes connection to database
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
     * Gets set of persons in database
     * @return Set of persons
     * @throws Exception throws exception if fails
     */
    @Override
    public Set<Person> personsSet() throws Exception {
        ResultSet resultSet = null;

        try {
            Set<Person> persons = new TreeSet<Person>();
            resultSet = getPersons.executeQuery();

            while (resultSet.next()) {
                Person person = new Person(resultSet.getInt("personID"), resultSet.getString("firstName"), resultSet.getString("lastName"));
                persons.add(person);
            }
            return persons;
        } catch (SQLException exception) {
            // Failed
            exception.printStackTrace();
            throw new Exception("SQL Error");

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Failed to get persons set");
        }
    }
}
