package services;

import dao.DataAccessException;
import dao.Database;
import dao.EventDao;
import dao.PersonDao;
import model.Event;
import model.Person;
import results.EventIDResult;
import results.EventResult;

import java.sql.Connection;
import java.util.ArrayList;

/** The EventService class returns a single event object or ALL events for ALL family members of the current user. The current
 user is determined from the provided auth token. */
public class EventService {

    /** The findEvent method takes an eventID and returns a EventIDResult object
     * @param eventID Contains the eventID of an event
     * @return result Contains the associatedUsername, eventID, personID, latitude, longitude, country, city,
     *                eventType, year, and success if successful
     *                OR Contains the message and success if unsuccessful
     */
    public EventIDResult findEvent(String eventID) throws DataAccessException {
        Database db = new Database();
        Connection conn = db.openConnection();
        Event event;

        try {
            EventDao eDao = new EventDao(conn);
            event = eDao.find(eventID);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw e;
        }

        return new EventIDResult(event);
    }

    /** The findEvents method takes an authToken and returns a EventResult object
     * @param userName Contains the userName of a user
     * @return result Contains the events of all family members of a user if successful
     *                OR Contains the message and success if unsuccessful
     */
    public EventResult findEvents(String userName) throws DataAccessException {

        Database db = new Database();
        Connection conn = db.openConnection();
        ArrayList<Event> familyEvents = null;

        try {
            EventDao eDao = new EventDao(conn);
            familyEvents = eDao.findAll(userName);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
            throw e;
        }

        return new EventResult(familyEvents);
    }

}
