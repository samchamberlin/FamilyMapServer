package services;

import dao.AuthTokenDao;
import dao.DataAccessException;
import dao.Database;
import dao.UserDao;
import model.AuthToken;
import model.User;
import requests.LoginRequest;
import results.LoginResult;

import javax.xml.crypto.Data;
import java.sql.Connection;

/** The LoginService logs in the user and returns an auth token. */
public class LoginService {

    /** The login method takes a LoginRequest object and returns a LoginResult object
     * @param request Contains the userName and password
     * @return result Contains the authToken, userName, personID, and success if successful
     *                OR Contains the message and success if unsuccessful
     */
    public LoginResult login(LoginRequest request) throws DataAccessException {
        Database db = new Database();
        Connection conn = null;
        LoginResult result = null;

        try {
            //Find user
            conn = db.openConnection();
            UserDao uDao = new UserDao(conn);
            User user = uDao.find(request.getUsername());
            if (user == null) {
                throw new DataAccessException("User not found!");
            }

            //Check password
            if (!user.getPassword().equals(request.getPassword())) {
                throw new DataAccessException("Wrong password!");
            }

            //Add auth token
            AuthToken authToken = new AuthToken(user.getUsername());
            AuthTokenDao aDao = new AuthTokenDao(conn);
            aDao.insert(authToken);

            // 4. Generate login response
            result = new LoginResult(authToken.getAuthToken(), user.getUsername(), user.getPersonID());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            throw new DataAccessException(e.toString());
        }

        return result;

    }

}
