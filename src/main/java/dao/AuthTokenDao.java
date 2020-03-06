package dao;

import model.AuthToken;

import java.sql.*;

/** AuthTokenDao interacts with the database to send and receive authorization token data */
public class AuthTokenDao {

    private final Connection conn;

    public AuthTokenDao(Connection conn) {
        this.conn = conn;
    }

    /** The insert method inserts an AuthToken object into the AuthToken table in the database
     * @param a Contains the event id, userName, person id, latitude, longitude, country, city, event type, and year
     * @return true if the AuthToken is inserted successfully, false if the AuthToken already exists
     */
    public void insert(AuthToken a) throws DataAccessException {
        String sql = "INSERT INTO AuthToken (authToken, associatedUsername) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, a.getAuthToken());
            stmt.setString(2, a.getUsername());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /** The find method finds an AuthToken object from the AuthToken table in the database
     * @param token The token to be found
     * @return authToken Contains authToken and the userName
     */
    public AuthToken find(String token) throws DataAccessException{
        AuthToken authToken;
        ResultSet rs = null;
        String sql = "SELECT * FROM AuthToken WHERE authToken = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authToken = new AuthToken(rs.getString("authToken"), rs.getString("associatedUsername"));
                return authToken;
            } else {
                throw new DataAccessException("AuthToken doesn't exist!");
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

    /** The delete method deletes an AuthToken object from the AuthToken table in the database
     * @param token The AuthToken to be deleted
     * @return true if the AuthToken is deleted successfully, false if the AuthToken is not found
     */
    public void delete(String token){
        String sql = "DELETE FROM AuthToken WHERE authToken = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            if(find(token) != null){
                stmt.execute();
            }
        } catch (DataAccessException | SQLException e) {
            e.printStackTrace();
        }
    }

    /** The clear method clears all AuthToken objects from the AuthToken table in the database
     * @return true if the AuthTokens are cleared successfully, false if not
     */
    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM AuthToken";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
}
