package passoff;

import dao.DataAccessException;
import dao.Database;
import dao.PersonDao;
import model.AuthToken;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import result.ClearResult;
import results.Result;
import services.ClearService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class ClearServiceTest {
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
        person1 = new Person("Jenny123A", "JC", "Jenny",
                "Chamberlin", 'F', "Bill123A",
                "Lynda123A", "David123A");
        person2 = new Person("David123A", "DC", "David",
                "Chamberlin", 'M', null,
                null, "Jenny123A");
        person3 = new Person("Lynda123A", "LJ", "Lynda",
                "Johnson", 'F', null,
                null, "Bill123A");
        person4 = new Person("Bill123A", "BJ", "Bill",
                "Johnson", 'M', null,
                null, "Lynda123A");
    }

    @AfterEach
    public void tearDown() throws Exception {
        Connection conn = db.openConnection();
        db.clearTables(conn);
        db.closeConnection(true);
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

            ClearService clearService = new ClearService();
            Result result = clearService.clear();

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
