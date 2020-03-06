package passoff;

import dao.DataAccessException;
import dao.Database;
import dao.UserDao;
import model.Event;
import model.User;
import org.junit.jupiter.api.*;
import requests.FillRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.RegisterResult;
import results.Result;
import services.FillService;
import services.RegisterService;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class FillServiceTest {

    private Result correctResponse;
    private RegisterRequest registerRequest;
    private String userName;
    private Database db;
    private int numGenerations;
    private User user1;

    @BeforeEach
    public void setUp() throws Exception {

        correctResponse =
                new Result("Successfully added 7 persons and 19 events to the database.", true);

        registerRequest = new RegisterRequest("sam", "pass", "sam123@gmail.com",
                                                "Sam", "Chamberlin", 'm');

        userName = "Sam";
        numGenerations = 2;
        db = new Database();

        user1 = new User("SamuelChamberlin", "snowboard1", "sam.chamberlin@hotmail.com",
                "Sam", "Chamberlin", 'm', "Sam123A");

    }

    @AfterEach
    public void tearDown() throws Exception {
        Connection conn = db.openConnection();
        db.clearTables(conn);
        db.closeConnection(true);
    }

    @Test
    public void fillPass() throws DataAccessException {

        // register a user
        RegisterService registerService = new RegisterService();
        LoginResult result = registerService.register(registerRequest);
//
//        // do the fill service
//        BasicResponse compareTest = null;
//        compareTest = FillService.fill(userName, numGenerations);
//
//        // check for correct response
//        Assert.assertEquals(correctResponse, compareTest);

    }

    @Test
    public void fillFail() throws DataAccessException, DataAccessException {

        //Try to fill a missing userName
        FillRequest request = new FillRequest(userName, numGenerations);
        FillService fillService = new FillService();
        Result result = fillService.fill(request);



//        User compareTest = null;
//
//        try {
//            Connection conn = db.openConnection();
//            UserDao uDao = new UserDao(conn);
//            uDao.insert(user1);
//            compareTest = uDao.find(bestUser.getUsername());
//            db.closeConnection(true);
//        } catch (DataAccessException e) {
//            db.closeConnection(false);
//        }
//        //First lets see if our find found anything at all. If it did then we know that if nothing
//        //else something was put into our database, since we cleared it in the beginning
//        assertNotNull(compareTest);
//        //Now lets make sure that what we put in is exactly the same as what we got out. If this
//        //passes then we know that our insert did put something in, and that it didn't change the
//        //data in any way
//        assertEquals(bestUser, compareTest);

    }


}
