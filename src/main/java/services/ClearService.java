package services;

import dao.DataAccessException;
import dao.Database;
import results.Result;

import java.sql.Connection;

/** The ClearService class deletes ALL data from the database, including user accounts, auth tokens, and generated
 * person and event data.
 */
public class ClearService {

    /** The clear method clears the database and returns a Result object
     * @return result Contains the message and success
     */
    public Result clear() throws DataAccessException {
        Database db = new Database();
        Connection conn = db.openConnection();
        db.clearTables(conn);
        db.closeConnection(true);
        return new Result("Clear Succeeded", true);
    }

}
