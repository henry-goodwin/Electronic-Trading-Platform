package com.company.Testing.UsersTest;

import com.company.Model.User;
import com.company.Testing.TestingException;
import com.company.Utilities.PasswordHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestUsers {

    User user;
    MockUsersDatabase mockUsersDatabase;

    /**
     * Before each test construct a user
     */
    @BeforeEach
    void constructUser() {
        try {
            user = new User(3, "Standard", 2, "hezza_123", PasswordHasher.hashString("fakePassword"));
        } catch (Exception e) {
            user = new User();
        }
    }

    @BeforeEach
    void constructMockDatabase() {
        mockUsersDatabase = new MockUsersDatabase();
    }

    /**
     * Test that set userID performs correctly
     * @throws TestingException Throw exception if test fails
     */
    @Test
    void TestSetUserID() throws TestingException {
        user.setUserID(5);
        assertEquals(5, user.getUserID());
    }

    /**
     * Test that exception is thrown when a negative userID is set
     * @throws TestingException Throw exception if set fails
     */
    @Test
    void TestSetNegativeUserID() throws TestingException {
        assertThrows(Exception.class, () -> {
           user.setUserID(-5);
        });
    }

    /**
     * Test that an accountType is set correctly
     * @throws Exception Throw exception if set fails
     */
    @Test
    void TestSetAccountType() throws TestingException {
        user.setAccountType("Admin");
        assertEquals("Admin", user.getAccountType());
    }

    /**
     * Test that an exception is thrown if an invalid account type is set
     * @throws TestingException Throw exception if set fails
     */
    @Test
    void TestSetInvalidAccountType() throws TestingException {
        assertThrows(Exception.class, () -> {
            user.setAccountType("type");
        });
    }

    /**
     * Test that an personID is set correctly
     * @throws TestingException Throw exception if set fails
     */
    @Test
    void TestSetPersonID() throws Exception {
        user.setPersonID(8);
        assertEquals(8, user.getPersonID());
    }

    /**
     * Test that an exception is thrown if an invalid personID is set
     * @throws TestingException Throw exception if set fails
     */
    @Test
    void TestSetInvalidPersonID() throws TestingException {
        assertThrows(Exception.class, () -> {
            user.setPersonID(-1);
        });
    }

    /**
     * Test that an username is set correctly
     * @throws TestingException Throw exception if set fails
     */
    @Test
    void TestSetUsername() throws TestingException {
        user.setUsername("the_Man89");
        assertEquals("the_Man89", user.getUsername());
    }

    /**
     * Test that an exception is thrown if an invalid username is set
     * @throws Exception Throw exception if set fails
     */
    @Test
    void TestSetInvalidUsername() throws TestingException {
        assertThrows(Exception.class, () -> {
            user.setUsername("fhd fhsdh &");
        });
    }

    /**
     * Test adding a valid user to database
     * @throws Exception Throw exception if add fails
     */
    @Test
    void TestAddUser() throws Exception {
        assertDoesNotThrow(() -> {
            mockUsersDatabase.addUser(user);
        });
    }

    /**
     * Test adding a invalid user to database
     * @throws Exception
     */
    @Test
    void TestAddInvalidUser() throws Exception {
        assertThrows(Exception.class, () -> {
           mockUsersDatabase.addUser(new User());
        });
    }

    @Test
    void TestChangePasswordValid() throws Exception {
        assertDoesNotThrow(() -> {
            mockUsersDatabase.changePassword(PasswordHasher.hashString("newPassword"), 2);
        });
    }

    @Test
    void TestChangePasswordInvalidPassword() throws Exception {
        assertThrows(Exception.class, () -> {
            mockUsersDatabase.changePassword(PasswordHasher.hashString("newP assword"), 2);
        });
    }

    @Test
    void TestChangePasswordInvalidID() throws Exception {
        assertThrows(Exception.class, () -> {
            mockUsersDatabase.changePassword(PasswordHasher.hashString("newPassword"), -2);
        });
    }

    @Test
    void TestUsernameAvailability() throws Exception {
        assertDoesNotThrow(() -> {
            mockUsersDatabase.checkUsernameAvailability("newUsername2");
        });
    }

    @Test
    void TestInvalidUsername() throws Exception {
        assertThrows(Exception.class, () -> {
           mockUsersDatabase.checkUsernameAvailability("bad username");
        });
    }

    @Test
    void TestTakenUsername() throws Exception {
        assertEquals(false, mockUsersDatabase.checkUsernameAvailability("davey"));

    }

    @Test
    void TestCheckPasswordValid() throws Exception {
        User user = new User(1, "Standard", 2, "davey", PasswordHasher.hashString("deadly"));
        assertEquals(true, mockUsersDatabase.checkPassword(user.getUserID(), user.getPasswordHash()));
    }

    @Test
    void TestCheckPasswordInValid() throws Exception {
        User user = new User(1, "Standard", 2, "davey", PasswordHasher.hashString("deadly"));
        assertEquals(false, mockUsersDatabase.checkPassword(user.getUserID(), "invalidPassword"));
    }

    @Test
    void TestLoginValid() throws Exception {
        User user = new User(1, "Standard", 2, "davey", PasswordHasher.hashString("deadly"));

        assertEquals(true, mockUsersDatabase.login(user.getUsername(), user.getPasswordHash()));
    }

    @Test
    void TestLoginInvalidUsername() throws Exception {
        User user = new User(1, "Standard", 2, "dag vey", PasswordHasher.hashString("deadly"));

        assertThrows(Exception.class, () -> {
            mockUsersDatabase.login(user.getUsername(), user.getPasswordHash());
        });
    }

    @Test
    void TestLoginInvalidPassword() throws Exception {
        User user = new User(1, "Standard", 2, "davey", PasswordHasher.hashString("wrong"));

        assertEquals(false, mockUsersDatabase.login(user.getUsername(), user.getPasswordHash()));

    }


}
