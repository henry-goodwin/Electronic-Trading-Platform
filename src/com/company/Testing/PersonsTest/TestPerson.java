package com.company.Testing.PersonsTest;

import com.company.Model.Person;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class TestPerson {

    private Person person;
    private MockPersonsDatabase mockPersonsDatabase;

    /**
     * Before each test construct a person
     */
    @BeforeEach
    void ConstructPerson() {
        try {
            person = new Person(3, "James", "Smith");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Before each test construct a database
     */
    @BeforeEach
    void ConstructDatabase() {
        mockPersonsDatabase = new MockPersonsDatabase();
    }

    /**
     * Test setting a valid ID
     * @throws Exception throws exception if test fails
     */
    @Test
    void TestSetIDValid() throws Exception {
        assertDoesNotThrow(() -> {
            person.setPersonID(4);
        });
        assertEquals(4, person.getPersonID());
    }

    /**
     * Test setting a invalid ID
     * @throws Exception throws exception if test fails
     */
    @Test
    void TestSetInvalidID() throws Exception {
        assertThrows(Exception.class, () -> {
            person.setPersonID(-4);
        });
    }

    /**
     * Test setting a valid firstName
     * @throws Exception
     */
    @Test
    void TestSetFirstnameValid() throws Exception {
        assertDoesNotThrow(() -> {
            person.setFirstname("Luke");
        });
        assertEquals("Luke", person.getFirstname());
    }

    /**
     * Test setting an invalid firstname
     */
    @Test
    void TestSetInvalidFirstname() throws Exception {
        assertThrows(Exception.class, () -> {
            person.setFirstname("");
        });
    }

    /**
     * Test setting a valid lastname
     */
    @Test
    void TestSetLastnameValid() throws Exception {
        assertDoesNotThrow(() -> {
            person.setLastname("Part");
        });
        assertEquals("Part", person.getLastname());
    }

    /**
     * Test adding a valid person
     */
    @Test
    void TestAddPerson() throws Exception {
        assertDoesNotThrow(() -> {
            mockPersonsDatabase.addPerson(new Person("Henry", "Goodwin"));
        });
    }

    /**
     * Test getting person from database
     * @throws Exception
     */
    @Test
    void TestGetPerson() throws Exception {
        assertDoesNotThrow(() -> {
            mockPersonsDatabase.getPerson(1);
        });

        assertEquals("James", mockPersonsDatabase.getPerson(1).getFirstname());

    }
}
