package com.company.Common.DataSource;

import com.company.Common.Model.Person;

import java.util.Set;

public interface PersonsDataSource {

    /**
     * Adds a User to the data, if they are not already in the database
     *
     * @param person Person to add
     */
    void addPerson(Person person) throws Exception;

    /**
     * Extracts all the details of a User from the database based on the
     * userID passed in.
     *
     * @param personID The personID as a Integer to search for.
     * @return all details in a Person object for the personID
     */
    Person getPerson(Integer personID) throws Exception;

    /**
     * Finalizes any resources used by the data source and ensures data is
     * persisited.
     */
    void close();

    /**
     * Retrieves a set of persons from the data source that are used in
     * the database.
     *
     * @return set of personIDs.
     */
    Set<Person> personsSet() throws Exception;

}
