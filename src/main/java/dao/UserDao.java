package dao;

import model.User;

import java.sql.*;

/** UserDao interacts with the database to send and receive user data */
public class UserDao {

    private final Connection conn;

    public UserDao(Connection conn) {
        this.conn = conn;
    }

    /** The insert method inserts a user object into the user table in the database
     * @param user Contains the userName, password, email, first name, last name, gender, and person id
     */
    public void insert(User user) throws DataAccessException{
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO User (userName, password, email, firstName, lastName, gender, " +
                "personID) VALUES(?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, String.valueOf(user.getGender()));
            stmt.setString(7, user.getPersonID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /** The find method finds a user object from the user table in the database
     * @param userName The userName of the user to be found
     * @return result Contains userName, password, email, first name, last name, gender, and person id
     */
    public User find(String userName) throws DataAccessException{
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM User WHERE userName = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("userName"), rs.getString("password"),
                        rs.getString("email"), rs.getString("firstName"), rs.getString("lastName"),
                        rs.getString("gender").charAt(0), rs.getString("personID"));
                return user;
            } else {
                throw new DataAccessException("Person does not exist!");
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

    /** The delete method deletes a user object from the user table in the database
     * @param userName The userName of the user to be deleted
     */
    public void delete(String userName){
        String sql = "DELETE FROM User WHERE userName = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            if(find(userName) != null){
                stmt.execute();
            }
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
        }
    }

    /** The clear method clears all user objects from the user table in the database
     * @return true if the users are cleared successfully, false if not
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM User";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }

}