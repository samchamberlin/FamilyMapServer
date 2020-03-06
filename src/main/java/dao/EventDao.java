package dao;

import model.Event;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;

/** EventDao interacts with the database to send and receive event data */
public class EventDao {

    private final Connection conn;

    public EventDao(Connection conn) {
        this.conn = conn;
    }

    /** The insert method inserts an event object into the event table in the database
     * @param event Contains the event id, userName, person id, latitude, longitude, country, city, event type, and year
     * @return true if the event is inserted successfully, false if the event already exists
     */
    public void insert(Event event) throws DataAccessException{
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Event (eventID, associatedUsername, personID, latitude, longitude, " +
                "country, city, eventType, year) VALUES(?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getAssociatedUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setDouble(4, event.getLatitude());
            stmt.setDouble(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /** The find method finds an event object from the event table in the database
     * @param eventID The event id of the event to be found
     * @return e Contains the event id, userName, person id, latitude, longitude, country, city, event type, and year
     */
    public Event find(String eventID) throws DataAccessException{
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM Event WHERE eventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getDouble("latitude"), rs.getDouble("longitude"),
                        rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                        rs.getInt("year"));
                return event;
            } else {
                throw new DataAccessException("Event doesn't exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /** The findAll method finds many event objects from the event table in the database
     * @param userName The userName of the user
     * @return e Contains all events for all family members of the user
     */
    public ArrayList<Event> findAll(String userName) throws DataAccessException {
        ArrayList<Event> events = new ArrayList<>();
        Event event = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM Event WHERE associatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            while (rs.next()) {
                event = new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getDouble("latitude"), rs.getDouble("longitude"),
                        rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                        rs.getInt("year"));
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding events!");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return events;
    }

    /** The delete method deletes an event object from the event table in the database
     * @param eventID The event id of the event to be deleted
     * @return true if the event is deleted successfully, false if the event is not found
     */
    public boolean delete(String eventID){
        return false;
    }

    /** The clear method clears all event objects from the event table in the database
     * @return true if the events are cleared successfully, false if not
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM Event";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }

}
