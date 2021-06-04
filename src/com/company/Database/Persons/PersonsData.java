package com.company.Database.Persons;

import com.company.Database.Users.UsersDataSource;
import com.company.Model.Person;
import com.company.Model.User;

import javax.swing.*;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class PersonsData {

    private DefaultListModel<Person> personDefaultListModel;
    private PersonsDataSource personsDataSource;

    /**
     * Constructor that initialises variables
     * @param dataSource
     * @throws Exception Throws exception if fails to get list
     */
    public PersonsData(PersonsDataSource dataSource) throws Exception {

        personsDataSource = dataSource;
        personDefaultListModel = new DefaultListModel<Person>();

        try {
            for (Person person : personsDataSource.personsSet()) {
                personDefaultListModel.addElement(person);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Get Persons List Failed");
        }
    }

    /**
     * Saves the data in the database using a persistence
     * mechanism.
     */
    public void persist() {
        personsDataSource.close();
    }

    /**
     * Retrieves Persons details from the model.
     *
     * @param key the id to retrieve.
     * @return the Person object related to the id.
     * @throws Exception Throws exception if fails
     */
    public Person get(Object key) throws Exception {
        return personsDataSource.getPerson((Integer) key);
    }


    /**
     * Accessor for the list model.
     *
     * @return the listModel to display.
     */
    public ListModel<Person> getPersonsModel() { return  personDefaultListModel; }


    /**
     * Adds a Person to the database.
     *
     * @param person Person to add to the database.
     * @throws Exception Throws exception if fails
     */
    public void addUser(Person person) throws Exception {
       personsDataSource.addPerson(person);
    }

}
