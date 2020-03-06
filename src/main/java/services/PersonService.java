package services;

import dao.DataAccessException;
import dao.Database;
import dao.PersonDao;
import model.Person;
import results.PersonIDResult;
import results.PersonResult;

import java.sql.Connection;
import java.util.ArrayList;

/** The PersonService class returns a single person or ALL family members of the current user. The current user is
 determined from the provided auth token */
public class PersonService {

    /** The getPerson method gets the person of a user
     * @param personID person id of person object to retrieve
     * @return person person to be returned
     */
    public static PersonIDResult getPerson(String personID) throws DataAccessException {

        Database db = new Database();
        Connection conn = db.openConnection();
        Person person;

        try {
            PersonDao pDao = new PersonDao(conn);
            person = pDao.find(personID);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw e;
        }

        return new PersonIDResult(person);
    }

    /** The getFamily method takes a userName and returns an array of person objects
     * @param personID Contains the ID of the person
     * @return result Contains the array of person objects which is the user's family
     */
    public PersonResult getFamily(String personID) throws DataAccessException {

        Database db = new Database();
        Connection conn = db.openConnection();
        ArrayList<Person> family;

        try {
            PersonDao pDao = new PersonDao(conn);
            family = pDao.findAll(personID);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw e;
        }

        return new PersonResult(family);
    }

}