package passoff;

import dao.*;
import model.AuthToken;
import model.Person;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenDaoTest {
    private Database db;
    private AuthToken authToken1;
    private AuthToken authToken2;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Database();
        authToken1 = new AuthToken("A123B","samc");
        authToken2 = new AuthToken("D123E","mattc");
    }

    @AfterEach
    public void tearDown() throws Exception {
        Connection conn = db.openConnection();
        db.clearTables(conn);
        db.closeConnection(true);
    }

    @Test
    public void insertPass() throws DataAccessException {
        AuthToken compareTest = null;
        try {
            Connection conn = db.openConnection();
            AuthTokenDao aDao = new AuthTokenDao(conn);
            aDao.insert(authToken1);
            compareTest = aDao.find(authToken1.getAuthToken());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            e.printStackTrace();
        }
        assertNotNull(compareTest);
        assertEquals(compareTest, authToken1);
    }

    @Test
    public void insertFail() throws Exception {
        boolean works = true;
        try {
            Connection conn = db.openConnection();
            AuthTokenDao aDao = new AuthTokenDao(conn);
            aDao.insert(authToken1);
            aDao.insert(authToken1);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            works = false;
        }
        assertFalse(works);
        AuthToken compareTest;
        try {
            Connection conn = db.openConnection();
            AuthTokenDao aDao = new AuthTokenDao(conn);
            compareTest = aDao.find(authToken1.getAuthToken());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            compareTest = null;
        }
        assertNull(compareTest);
    }

    @Test
    public void findPass() throws Exception {
        AuthToken compareTest = null;
        try {
            Connection conn = db.openConnection();
            AuthTokenDao aDao = new AuthTokenDao(conn);
            aDao.insert(authToken1);
            aDao.insert(authToken2);
            compareTest = aDao.find(authToken1.getAuthToken());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(authToken1, compareTest);
    }

    @Test
    public void findFail() throws Exception {
        boolean works = true;
        AuthToken compareTest = null;
        try {
            Connection conn = db.openConnection();
            AuthTokenDao aDao = new AuthTokenDao(conn);
            aDao.insert(authToken1);
            compareTest = aDao.find(authToken2.getAuthToken());
            //Here we should throw an exception
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            works = false;
        }
        //Check to make sure that we did in fact enter our catch statement
        assertFalse(works);
    }

    @Test
    public void deletePass() throws Exception {
        AuthToken compareTest = null;
        boolean works = false;
        try {
            Connection conn = db.openConnection();
            AuthTokenDao aDao = new AuthTokenDao(conn);
            aDao.insert(authToken1);
            aDao.insert(authToken2);
            compareTest = aDao.find(authToken1.getAuthToken());
            assertNotNull(compareTest);
            aDao.delete(authToken1.getAuthToken());
            //This should throw an exception
            aDao.find(authToken1.getAuthToken());
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
            AuthTokenDao aDao = new AuthTokenDao(conn);
            aDao.insert(authToken1);
            aDao.insert(authToken2);
            aDao.clear();
            aDao.find(authToken1.getAuthToken());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            didItWork = false;
        }
        //Check to make sure that we did in fact enter our catch statement
        assertFalse(didItWork);
    }
}
