package passoff;

import dao.DataAccessException;
import dao.Database;
import dao.EventDao;
import dao.PersonDao;
import model.Event;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class EventDaoTest {
    private Database db;
    private Event bestEvent;
    private Event event1;
    private Event event2;

    @BeforeEach
    public void setUp() throws Exception {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Database();
        //and a new event with random data
        bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        event1 = new Event("Biking_123B", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        event2 = new Event("Biking_123C", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
    }

    @AfterEach
    public void tearDown() throws Exception {
        //here we can get rid of anything from our tests we don't want to affect the rest of our program
        //lets clear the tables so that any data we entered for testing doesn't linger in our files
        Connection conn = db.openConnection();
        db.clearTables(conn);
        db.closeConnection(true);
    }

    //#########################   Test Inserts    #########################

    @Test
    public void insertPass() throws Exception {
        //We want to make sure insert works
        //First lets create an Event that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        Event compareTest = null;

        try {
            //Let's get our connection and make a new Dao
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            //While insert returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            eDao.insert(bestEvent);
            //So lets use a find method to get the event that we just put in back out
            compareTest = eDao.find(bestEvent.getEventID());
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
        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;
        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            eDao.insert(bestEvent);
            eDao.insert(bestEvent);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            didItWork = false;
        }
        assertFalse(didItWork);
        Event compareTest = null;
        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            compareTest = eDao.find(bestEvent.getEventID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(compareTest);
    }

    //#########################   Test Finds    #########################

    @Test
    public void findPass() throws Exception {
        Event compareTest = null;

        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            eDao.insert(bestEvent);
            compareTest = eDao.find(bestEvent.getEventID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void findFail() throws Exception {
        boolean works = false;
        Event compareTest = null;

        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            eDao.insert(bestEvent);
            compareTest = eDao.find("Biking_123B");
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            works = true;
        }
        assertTrue(works);
    }

    @Test
    public void findAllPass() throws Exception {
        ArrayList<Event> compareTest = null;
        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            eDao.insert(bestEvent);
            eDao.insert(event1);
            eDao.insert(event2);
            compareTest = eDao.findAll("Gale123A");
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNotNull(compareTest);
    }

    //#########################   Test Deletes    #########################
}
