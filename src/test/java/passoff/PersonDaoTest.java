package passoff;

import dao.DataAccessException;
import dao.Database;
import dao.PersonDao;
import dao.UserDao;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDaoTest {
    private Database db;
    private Person bestPerson;
    private Person person1;
    private Person person2;
    private Person person3;
    private Person person4;

    @BeforeEach
    public void setUp() throws Exception {
        //Set up test variables with random data
        db = new Database();
        bestPerson = new Person("Sam123A", "SamuelChamberlin", "Sam",
                "Chamberlin", 'M', "David123A",
                "Jenny123A", "Alexa123A");
        person1 = new Person("Jenny123A", "SamuelChamberlin", "Jenny",
                "Chamberlin", 'F', "Bill123A",
                "Lynda123A", "David123A");
        person2 = new Person("David123A", "SamuelChamberlin", "David",
                "Chamberlin", 'M', null,
                null, "Jenny123A");
        person3 = new Person("Lynda123A", "SamuelChamberlin", "Lynda",
                "Johnson", 'F', null,
                null, "Bill123A");
        person4 = new Person("Bill123A", "SamuelChamberlin", "Bill",
                "Johnson", 'M', null,
                null, "Lynda123A");
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
        //First lets create a Person that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        Person compareTest = null;

        try {
            //Let's get our connection and make a new Dao
            Connection conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);
            pDao.insert(bestPerson);
            //Use find method to get the person that we just put in back out
            compareTest = pDao.find(bestPerson.getPersonID());
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
        assertEquals(bestPerson, compareTest);

    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;
        try {
            Connection conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);
            //This should insert successfully
            pDao.insert(bestPerson);
            //personID is unique so this should throw an exception now
            pDao.insert(bestPerson);
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
        Person compareTest;
        try {
            Connection conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);
            //and then get something back from our find. If the person is not in the database we
            //should throw an exception
            compareTest = pDao.find(bestPerson.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            //Change compareTest to null since the person isn't in the database
            compareTest = null;
        }

        //Now make sure that compareTest is indeed null
        assertNull(compareTest);
    }

    @Test
    public void findPass() throws Exception {
        //We want to make sure insert works
        //First lets create an Event that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        Person compareTest = null;

        try {
            //Let's get our connection and make a new Dao
            Connection conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);
            //While insert returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            pDao.insert(bestPerson);
            pDao.insert(person1);
            pDao.insert(person2);
            compareTest = pDao.find(person1.getPersonID());
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
        assertEquals(person1, compareTest);
    }

    @Test
    public void findFail() throws Exception {
        boolean didItWork = true;
        Person compareTest = null;
        try {
            Connection conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);
            //Insert 2 people
            pDao.insert(bestPerson);
            pDao.insert(person1);
            //Look for someone who hasn't yet been inputted
            compareTest = pDao.find(person2.getPersonID());
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
    public void findAllPass() throws Exception {
        ArrayList<Person> compareTest = null;
        try {
            Connection conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);
            pDao.insert(bestPerson);
            pDao.insert(person1);
            pDao.insert(person2);
            pDao.insert(person3);
            pDao.insert(person4);
            compareTest = pDao.findAll(bestPerson.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assert(compareTest.size() == 5);
    }

    @Test
    public void deletePass() throws Exception {
        Person compareTest = null;
        boolean works = false;
        try {
            Connection conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);
            pDao.insert(bestPerson);
            pDao.insert(person1);
            compareTest = pDao.find(person1.getPersonID());
            assertNotNull(compareTest);
            pDao.delete(person1.getPersonID());
            //This should throw an exception
            pDao.find(person1.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            works = true;
        }
        assert(works);
    }

    @Test
    public void clearPass() throws Exception {
        boolean didItWork = true;
        try {
            Connection conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);
            //Insert people
            pDao.insert(bestPerson);
            pDao.insert(person1);
            pDao.insert(person2);
            pDao.clear();
            pDao.find(bestPerson.getPersonID());
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
