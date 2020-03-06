package handler;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import dao.*;
import model.AuthToken;
import model.Person;
import model.User;
import requests.FillRequest;
import results.PersonIDResult;
import results.PersonResult;
import results.Result;
import services.FillService;
import services.PersonService;

public class PersonHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("Person Handler");

        boolean success = false;
        Gson gson = new Gson();

        try {

            if (exchange.getRequestMethod().toLowerCase().equals("get")) {

                boolean isFamilyAPI = false;
                String resultJson;

                //Get personID & determine API call type
                String path = exchange.getRequestURI().getPath();
                String[] URI = path.split("/");
                String personID = null;
                try{
                    personID = URI[2];
                } catch (Exception e) {
                    isFamilyAPI = true;
                }

                //authenticate
                Headers requestHeaders = exchange.getRequestHeaders();
                String authToken = requestHeaders.getFirst("Authorization");
                String userName = authenticate(authToken);

                //get person else get family
                if(!isFamilyAPI){
                    // get associated person
                    PersonService personService = new PersonService();
                    PersonIDResult result = personService.getPerson(personID);

                    //ensure that this personID belongs to the user with the auth token
                    Database db = new Database();
                    Connection conn = db.openConnection();
                    UserDao uDao = new UserDao(conn);
                    User user = uDao.find(userName);
                    db.closeConnection(true);
                    if (!user.getPersonID().equals(personID)) {
                        throw new DataAccessException("This person does not belong to " + userName);
                    }

                    resultJson = gson.toJson(result);
                } else {
                    //get personID and family
                    Database db = new Database();
                    Connection conn = db.openConnection();
                    User user = null;
                    try {
                        UserDao uDao = new UserDao(conn);
                        user = uDao.find(userName);
                        db.closeConnection(true);
                    } catch (DataAccessException e) {
                        db.closeConnection(false);
                        e.printStackTrace();
                    }

                    PersonService personService = new PersonService();
                    PersonResult result = personService.getFamily(user.getPersonID());

                    resultJson = gson.toJson(result);
                }

                //write result
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream respBody = exchange.getResponseBody();
                writeString(resultJson, respBody);
                respBody.close();
                success = true;

            }

            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                Result error = new Result("Invalid request.", false);
                String errorJson = gson.toJson(error);
                OutputStream respBody = exchange.getResponseBody();
                writeString(errorJson, respBody);
                respBody.close();
            }
        }
        catch (IOException | DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            Result error = new Result("error " + e.getMessage(), false);
            String errorJson = gson.toJson(error);
            OutputStream respBody = exchange.getResponseBody();
            writeString(errorJson, respBody);
            respBody.close();

            e.printStackTrace();
        }
    }

    /*
        The writeString method shows how to write a String to an OutputStream.
    */
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    private String authenticate(String authToken) throws DataAccessException {

        Database db = new Database();
        Connection conn = db.openConnection();
        String userName;

        try {
            AuthTokenDao aDao = new AuthTokenDao(conn);
            AuthToken token = aDao.find(authToken);
            userName = token.getUsername();
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            e.printStackTrace();
            throw e;
        }

        return userName;
    }
}