package com.company.Database.Persons;

import com.company.Model.Person;
import com.company.Model.User;

import javax.swing.*;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class PersonsData {

    private DefaultListModel<Person> personDefaultListModel;
    private PersonsDataSource personsDataSource;

    public PersonsData() {

        personsDataSource = new JDBCPersonsDataSource();

        personDefaultListModel = new DefaultListModel<Person>();

        for (Person person : personsDataSource.personsSet()) {
            personDefaultListModel.addElement(person);
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
     */
    public Person get(Object key) {
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
     */
    public void addUser(Person person) {
       personsDataSource.addPerson(person);
    }

}
