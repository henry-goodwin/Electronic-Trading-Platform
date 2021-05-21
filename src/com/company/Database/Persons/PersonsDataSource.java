package com.company.Database.Persons;

import com.company.Model.Person;
import com.company.Model.User;

import java.util.Set;
import java.util.TreeMap;

public interface PersonsDataSource {

    /**
     * Adds a User to the datebase, if they are not already in the datebase
     *
     * @param person Person to add
     */
    void addPerson(Person person);

    /**
     * Extracts all the details of a User from the database based on the
     * userID passed in.
     *
     * @param personID The personID as a Integer to search for.
     * @return all details in a Person object for the personID
     */
    Person getPerson(Integer personID);

    /**
     * Finalizes any resources used by the data source and ensures data is
     * persisited.
     */
    void close();

    /**
     * Retrieves a set of personIDs from the data source that are used in
     * the database.
     *
     * @return set of personIDs.
     */
    Set<Integer> personIDSet();

    Set<Person> personsSet();

}
