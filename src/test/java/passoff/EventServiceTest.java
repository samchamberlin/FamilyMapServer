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
public class EventServiceTest {
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
        Connection conn = db.openConnection();
        db.clearTables(conn);
        db.closeConnection(true);
    }

    @Test
    public void insertPass() throws Exception {
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

}
