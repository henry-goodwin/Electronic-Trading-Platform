package com.company.Common.Model;

import java.io.Serializable;

public class Person implements Comparable<Person>, Serializable {

    // Private class variables
    private Integer personID;
    private String firstname;
    private String lastname;

    // Constructor
    public Person() { }

    /**
     * Constructor to set values for the Person's details.
     * @param personID personID to set
     * @param firstname firstName to set
     * @param lastname lastname to set
     */
    public Person(Integer personID, String firstname, String lastname) throws Exception {
        try {
            setPersonID(personID);
            setFirstname(firstname);
            setLastname(lastname);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Constructor to set values for the Person's details.
     * @param firstname firstname to set
     * @param lastname lastname to set
     */
    public Person(String firstname, String lastname) throws Exception {
        try {
            setFirstname(firstname);
            setLastname(lastname);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Gets full name of user
     * @return full name of user
     */
    public String toString() { return (this.firstname + " " + this.lastname); }

    /**
     * Gets the person ID
     * @return the personID
     */
    public Integer getPersonID() {return this.personID;}

    /**
     * Sets the person ID
     * @param personID the personID to set
     * @throws Exception Throws exception if personID is invalid
     */
    public void setPersonID(Integer personID) throws Exception {
        if (personID < 0) throw new Exception("Invalid Person ID");
        this.personID = personID;
    }

    /**
     * Gets the fistname of the person
     * @return the firstname
     */
    public String getFirstname() {return this.firstname;}

    /**
     * Sets the firstname of the user
     * @param firstname the lastname to set
     * @throws Exception Throws exception if no firstname is entered
     */
    public void setFirstname(String firstname) throws Exception {
        if (firstname.equals("")) throw new Exception("Error, invalid firstname");
        if (!firstname.matches("(^[a-zA-Z]+$)")) throw new Exception("Error: Please ensure name have no numbers or symbols");
        this.firstname = firstname;
    }

    /**
     * Gets the last name of the user
     * @return the lastname
     */
    public String getLastname() {return this.lastname;}

    /**
     * Sets the lastname of the user
     * @param lastname the lastname to set
     * @throws Exception throws exception if lastname is empty
     */
    public void setLastname(String lastname) throws Exception {
        if (lastname.equals("")) throw new Exception("No lastname was entered");
        if (!lastname.matches("(^[a-zA-Z]+$)")) throw new Exception("Error: Please ensure name have no numbers or symbols");
        this.lastname = lastname;
    }

    /**
     * Compares this object with the specified object for order. Returns a
     * negative integer, zero, or a positive integer as this object is less than,
     * equal to, or greater than the specified object.
     *
     * @param other The other Person object to compare against.
     * @return a negative integer, zero, or a positive integer as this object is
     *         less than, equal to, or greater than the specified object.
     * @throws ClassCastException if the specified object's type prevents it from
     *            being compared to this object.
     */

    @Override
    public int compareTo(Person other) {
        return this.lastname.compareTo(other.lastname);
    }
}
