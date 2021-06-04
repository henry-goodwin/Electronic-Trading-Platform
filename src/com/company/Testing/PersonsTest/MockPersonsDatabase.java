package com.company.Testing.PersonsTest;

import com.company.Database.Persons.PersonsDataSource;
import com.company.Model.Person;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class MockPersonsDatabase implements PersonsDataSource {

    TreeMap<Integer, Person> data;

    public MockPersonsDatabase() {
        data = new TreeMap<Integer, Person>();
        try {
            data.put(data.size()+1, new Person(data.size()+1, "James", "Franklyn"));
            data.put(data.size()+1, new Person(data.size()+1, "Craig", "Smuth"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Add person to database
     * @param person Person to add
     * @throws Exception throws exception if person name is invalid
     */
    @Override
    public void addPerson(Person person) throws Exception {
        if (person.getFirstname().equals("") || person.getLastname().equals("")) throw new Exception("Invalid  name");

        data.put(data.size()+1, person);

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

        return data.get(personID);
    }

    @Override
    public void close() {

    }

    /**
     * Gets set of persons in database
     * @return Set of persons
     * @throws Exception throws exception if fails
     */
    @Override
    public Set<Person> personsSet() throws Exception {
        Set<Person> userSet = new TreeSet<Person>();

        for(Integer key: data.keySet()) {
            userSet.add(data.get(key));
        }
        return userSet;
    }
}
