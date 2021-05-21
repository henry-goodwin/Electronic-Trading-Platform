package com.company.Model;

import java.io.Serializable;

public class Person implements Comparable<Person>, Serializable {

    private int personID;
    private String firstname;
    private String lastname;

    public Person() { }

    /**
     * Constructor to set values for the Person's details.
     * @param personID
     * @param firstname
     * @param lastname
     */
    public Person(Integer personID, String firstname, String lastname) {
        this.personID = personID;
        this.firstname= firstname;
        this.lastname = lastname;
    }

    /**
     * Constructor to set values for the Person's details.
     * @param firstname
     * @param lastname
     */
    public Person(String firstname, String lastname) {
        this.firstname= firstname;
        this.lastname = lastname;
    }

    public String toString() { return (this.firstname + " " + this.lastname); }

    /**
     * @return the personID
     */
    public Integer getPersonID() {return this.personID;}

    /**
     * @param personID the personID to set
     */
    public void setPersonID(Integer personID) {this.personID = personID;}

    /**
     * @return the firstname
     */
    public String getFirstname() {return this.firstname;}

    /**
     * @param firstname the lastname to set
     */
    public void setFirstname(String firstname) {this.firstname = firstname;}

    /**
     * @return the lastname
     */
    public String getLastname() {return this.lastname;}

    /**
     * @param lastname the lastname to set
     */
    public void setLastname(String lastname) {this.lastname = lastname;}

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
