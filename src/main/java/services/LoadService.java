package services;

import dao.*;
import model.Event;
import model.Person;
import model.User;
import requests.LoadRequest;
import results.Result;

import java.sql.Connection;
import java.util.ArrayList;

/** The LoadService class clears all data from the database (just like the /clear API), and then loads the
 posted user, person, and event data into the database. */
public class LoadService {

    /** The load method takes a LoadRequest object and returns a Result object
     * @param request Contains the users, persons, and events
     * @return result Contains the message and success
     */
    public Result load(LoadRequest request) throws DataAccessException {
        ClearService cs = new ClearService();
        cs.clear();

        ArrayList<User> users = request.getUsers();
        ArrayList<Person> persons = request.getPersons();
        ArrayList<Event> events = request.getEvents();

        Database db = new Database();

        int numUsers = 0;
        int numPersons = 0;
        int numEvents = 0;

        try {

            // insert all of them into the database
            Connection conn = db.openConnection();
            UserDao uDao = new UserDao(conn);
            PersonDao pDao = new PersonDao(conn);
            EventDao eDao = new EventDao(conn);

            numUsers = users.size();
            numPersons = persons.size();
            numEvents = events.size();

            for(int i = 0; i < numUsers; i++) {
                uDao.insert(users.get(i));
            }
            for(int i = 0; i < numPersons; i++) {
                pDao.insert(persons.get(i));
            }
            for(int i = 0; i < numEvents; i++) {
                eDao.insert(events.get(i));
            }

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        return new Result("Successfully added " + numUsers + " users, " + numPersons + " persons, and " +
                numEvents + " events.", true);
    }

}