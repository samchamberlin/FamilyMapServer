package dao;

import model.Event;
import model.Person;

import java.sql.*;
import java.util.ArrayList;

/** PersonDao interacts with the database to send and receive person data */
public class PersonDao {

    private final Connection conn;

    public PersonDao(Connection conn) {
        this.conn = conn;
    }

    /** The insert method inserts a person object into the person table in the database
     * @param person Contains the person id, userName, first name, last name, gender, father id, mother id, and spouse id
     */
    public void insert(Person person) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Person (personID, associatedUsername, firstName, lastName, gender, " +
                "fatherID, motherID, spouseID) VALUES(?,?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, String.valueOf(person.getGender()));
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /** The find method finds a person object from the person table in the database
     * @param personID The person id of the person to be found
     * @return p Contains the person id, userName, first name, last name, gender, father id, mother id, and spouse id
     */
    public Person find(String personID) throws DataAccessException {
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Person WHERE personID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                        rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender").charAt(0),
                        rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID"));
                return person;
            } else {
                throw new DataAccessException("Person doesn't exist!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
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

    /** The findAll method finds many person objects from the person table in the database
     * @param personID The person id of the person's family to be found
     * @return p Contains the family represented as an array of person objects
     */
    public ArrayList<Person> findAll(String personID) throws DataAccessException {
        ArrayList<Person> persons = new ArrayList<>();
        Person person1 = find(personID);
        String userName = person1.getAssociatedUsername();
        Person person = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM Person WHERE associatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            while (rs.next()) {
                person = new Person(rs.getString("personID"), rs.getString("associatedUsername"),
                        rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender").charAt(0),
                        rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID"));
                persons.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding persons!");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return persons;
    }

    /** The delete method deletes a person object from the person table in the database
     * @param personID The userName of the person to be deleted
     */
    public void delete(String personID){
        String sql = "DELETE FROM Person WHERE personID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            if(find(personID) != null){
                stmt.execute();
            }
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
        }
    }

    /** The clear method clears all person objects from the person table in the database
     * @return true if the persons are cleared successfully, false if not
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM Person";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }

}
