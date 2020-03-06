package services;

import dao.*;
import model.AuthToken;
import model.Person;
import model.User;
import requests.FillRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import results.LoginResult;
import results.RegisterResult;
import results.Result;

import java.sql.Connection;
import java.util.UUID;

/** The RegisterService class creates a new user account, generates 4 generations of ancestor data for the new
 * user, logs the user in, and returns an auth token.
 */
public class RegisterService {

    /** The register method takes a RegisterRequest object and returns a RegisterResult object
     * @param request Contains the userName, password, email, firstName, lastName, and gender
     * @return result Contains the authToken, userName, personID, and success if successful
     *                OR Contains the message and success if unsuccessful
     */
    public static LoginResult register(RegisterRequest request) throws DataAccessException {

        LoginResult result;
        Database db = new Database();
        Person person = null;
        User user = null;


        try {

            //create person
            String personID = UUID.randomUUID().toString().substring(0, 8);
            person = new Person(personID, request.getUsername(), request.getFirstName(), request.getLastName(),
                                request.getGender(), "", "", "");

            //create user
            user = new User(request.getUsername(), request.getPassword(), request.getEmail(), request.getFirstName(),
                            request.getLastName(), request.getGender(), personID);

            //insert into database
            Connection conn = db.openConnection();
            UserDao uDao = new UserDao(conn);
            PersonDao pDao = new PersonDao(conn);
            uDao.insert(user);
            pDao.insert(person);
            db.closeConnection(true);

            //fill
            FillRequest fillRequest = new FillRequest(user.getUsername(), 4);
            FillService fillService = new FillService();
            fillService.fill(fillRequest);

//            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            e.printStackTrace();
            throw e;
        }

        //login
        LoginRequest loginRequest = new LoginRequest(user.getUsername(), user.getPassword());
        LoginService loginService = new LoginService();
        result = loginService.login(loginRequest);

        return result;
    }
}
