package passoff;

import dao.DataAccessException;
import dao.Database;
import dao.UserDao;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class UserDaoTest {
    private Database db;
    private User bestUser;
    private User worstUser;

    @BeforeEach
    public void setUp() throws Exception {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        bestUser = new User("SamuelChamberlin", "snowboard1", "sam.chamberlin@hotmail.com",
                "Sam", "Chamberlin", 'M', "Sam123A");
        worstUser = new User("samc2", "snowboard12", "sam.chamberlin1@hotmail.com",
                "Sam", "Chamberlin", 'M', "Sam123B");
    }

    @AfterEach
    public void tearDown() throws Exception {
        //here we can get rid of anything from our tests we don't want to affect the rest of our program
        //lets clear the tables so that any data we entered for testing doesn't linger in our files
        Connection conn = db.openConnection();
        db.clearTables(conn);
        db.closeConnection(true);
    }

    @Test
    public void insertPass() throws Exception {
        //We want to make sure insert works
        //First lets create an Event that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        User compareTest = null;

        try {
            //Let's get our connection and make a new Dao
            Connection conn = db.openConnection();
            UserDao uDao = new UserDao(conn);
            //While insert returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            uDao.insert(bestUser);
            //So lets use a find method to get the event that we just put in back out
            compareTest = uDao.find(bestUser.getUsername());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(bestUser, compareTest);

    }

    @Test
    public void insertFail() throws Exception {
        //lets do this test again but this time lets try to make it fail

        // NOTE: The correct way to test for an exception in Junit 5 is to use an assertThrows
        // with a lambda function. However, lambda functions are beyond the scope of this class
        // so we are doing it another way.
        boolean didItWork = true;
        try {
            Connection conn = db.openConnection();
            UserDao uDao = new UserDao(conn);
            //if we call the method the first time it will insert it successfully
            uDao.insert(bestUser);
            //but our sql table is set up so that "eventID" must be unique. So trying to insert it
            //again will cause the method to throw an exception
            uDao.insert(bestUser);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            //If we catch an exception we will end up in here, where we can change our boolean to
            //false to show that our function failed to perform correctly
            db.closeConnection(false);
            didItWork = false;
        }
        //Check to make sure that we did in fact enter our catch statement
        assertFalse(didItWork);

        //Since we know our database encountered an error, both instances of insert should have been
        //rolled back. So for added security lets make one more quick check using our find function
        //to make sure that our event is not in the database
        //Set our compareTest to an actual event
        User compareTest = bestUser;
        try {
            Connection conn = db.openConnection();
            UserDao uDao = new UserDao(conn);
            //and then get something back from our find. If the event is not in the database we
            //should have just changed our compareTest to a null object
            compareTest = uDao.find(bestUser.getUsername());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            compareTest = null;
        }

        //Now make sure that compareTest is indeed null
        assertNull(compareTest);
    }

    @Test
    public void findPass() throws Exception {
        //We want to make sure find works
        //First lets create an User that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        User compareTest = null;

        try {
            //Let's get our connection and make a new Dao
            Connection conn = db.openConnection();
            UserDao uDao = new UserDao(conn);
            //While insert returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            uDao.insert(bestUser);
            uDao.insert(worstUser);
            compareTest = uDao.find(worstUser.getUsername());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(worstUser, compareTest);
    }

    @Test
    public void findFail() throws Exception {
        boolean didItWork = true;
        User compareTest = null;
        try {
            Connection conn = db.openConnection();
            UserDao uDao = new UserDao(conn);
            //Insert user
            uDao.insert(bestUser);
            //Look for someone who hasn't yet been inputted
            compareTest = uDao.find(worstUser.getUsername());
            //Here we should throw an exception
            db.closeConnection(true);
        } catch (DataAccessException e) {
            //If we catch an exception we will end up in here, where we can change our boolean to
            //false to show that our function failed to perform correctly
            db.closeConnection(false);
            didItWork = false;
        }
        //Check to make sure that we did in fact enter our catch statement
        assertFalse(didItWork);
    }

    @Test
    public void deletePass() throws Exception {
        User compareTest = null;
        try {
            Connection conn = db.openConnection();
            UserDao uDao = new UserDao(conn);
            uDao.insert(bestUser);
            uDao.insert(worstUser);
            uDao.delete(worstUser.getUsername());
            compareTest = uDao.find(worstUser.getUsername());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(compareTest);
    }

    @Test
    public void clearPass() throws Exception {
        boolean didItWork = true;
        try {
            Connection conn = db.openConnection();
            UserDao uDao = new UserDao(conn);
            //Insert users
            uDao.insert(bestUser);
            uDao.insert(worstUser);
            uDao.clear();
            uDao.find(bestUser.getUsername());
            //Should throw exception here bc person is not found
            db.closeConnection(true);
        } catch (DataAccessException e) {
            //If we catch an exception we will end up in here, where we can change our boolean to
            //false to show that our function failed to perform correctly
            db.closeConnection(false);
            didItWork = false;
        }
        //Check to make sure that we did in fact enter our catch statement
        assertFalse(didItWork);

    }

}
