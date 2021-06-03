package com.company.Testing;

import com.company.Database.OrganisationUnit.JDBCOrganisationUnitDataSource;
import com.company.Database.OrganisationUnit.OrganisationUnitData;
import com.company.Model.OrganisationUnit;
import static org.junit.jupiter.api.Assertions.*;

import com.company.NetworkDataSource.OrganisationUnitNDS;
import org.junit.jupiter.api.*;

public class TestOrgUnit {

    OrganisationUnit organisationUnit;
//    OrganisationUnitData organisationUnitData;
    JDBCOrganisationUnitDataSource jdbcOrganisationUnitDataSource;
    OrganisationUnitNDS organisationUnitNDS;

    /**
     * Before each test, create new Organisation Unit
     */
    @BeforeEach
    public void ConstructOrganisationUnit() {
        organisationUnit = new OrganisationUnit(1,"Sales",300.0);
        jdbcOrganisationUnitDataSource = new JDBCOrganisationUnitDataSource();
        organisationUnitNDS = new OrganisationUnitNDS();
    }

    /**
     * Test that to string returns the organisational unit string
     * @throws Exception throw exception if wrong organisational unit name is returned
     */
    @Test
    public void TestToString() throws Exception {
        assertEquals("Sales", organisationUnit.toString());
    }

    /**
     * Test that getID returns the organisational unit ID
     * @throws Exception throws exception if wrong organisational unitID is returned
     */
    @Test
    void TestGetID() throws Exception {
        assertEquals(1, organisationUnit.getID());
    }

    /**
     * Test that setID sets the ID correctly
     * @throws Exception throws exception if wrong organisational unit ID is set
     */
    @Test
    void TestSetID() throws Exception {
        organisationUnit.setID(2);
        assertEquals(2, organisationUnit.getID());
    }

    /**
     * Test that getName gets the correct name
     * @throws Exception Throws exception if wrong name is returned
     */
    @Test
    void TestGetName() throws Exception {
        assertEquals("Sales", organisationUnit.getName());
    }

    /**
     * Test that organisational name is set correctly
     * @throws Exception
     */
    @Test
    void TestSetName() throws Exception {
        organisationUnit.setName("Service");
        assertEquals("Service", organisationUnit.getName());
    }

    /**
     * Test that getCredits gets the correct credit quantity
     * @throws Exception Throws exception if getCredits returns wrong credits
     */
    @Test
    void TestGetCredits() throws Exception {
        assertEquals(300, organisationUnit.getCredits());
    }

    /**
     * Test the setCredits sets the correct credit quantity
     * @throws Exception Throws exception if wrong credit amount is set
     */
    @Test
    void TestSetCredits() throws Exception {
        organisationUnit.setCredits(100.0);
        assertEquals(100.0, organisationUnit.getCredits());
    }

    /**
     * Tests that setCredits throws error for negative number
     * @throws Exception Throws error if a negative number is entered
     */
    @Test
    void TestNegativeSetCredits() throws Exception {
        assertThrows(Exception.class, () -> {
            organisationUnit.setCredits(-100.0);
        });
        assertEquals(300.0, organisationUnit.getCredits());
    }

    /**
     * Tests add credit function
     * @throws Exception Throws exception if wrong credits are returned
     */
    @Test
    void TestAddCredits() throws Exception {
        organisationUnit.addCredits(77.5);
        assertEquals(377.5, organisationUnit.getCredits());
    }

    /**
     * Tests add credit function
     * @throws Exception Throws exception if a negative number is entered
     */
    @Test
    void TestAddNegativeCredits() throws Exception {
        assertThrows(Exception.class, () -> {
            organisationUnit.addCredits(-50.0);
        });
    }

    /**
     * Tests remove credit function
     * @throws Exception Throws exception if wrong credits are returned
     */
    @Test
    void TestRemoveCredits() throws Exception {
        assertThrows(Exception.class, () -> {
            organisationUnit.removeCredits(500.0);
        });

        organisationUnit.removeCredits(77.5);

        assertEquals(222.5, organisationUnit.getCredits());
    }

    /**
     * Tests remove credit function
     * @throws Exception Throws exception if a number greater than the organisations credit limit is entered
     */
    @Test
    void TestTooLargeRemoveCredits() throws Exception {
        assertThrows(Exception.class, () -> {
            organisationUnit.removeCredits(500.0);
        });
    }
}
