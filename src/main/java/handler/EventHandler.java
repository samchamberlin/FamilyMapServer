package handler;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import dao.*;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import results.EventIDResult;
import results.EventResult;
import results.Result;
import services.EventService;
import services.PersonService;

public class EventHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("Event Handler");

        boolean success = false;
        Gson gson = new Gson();

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {

                boolean isFamilyAPI = false;
                String resultJson;

                //Get eventID & determine API call type
                String path = exchange.getRequestURI().getPath();
                String[] URI = path.split("/");
                String eventID = null;
                try{
                    eventID = URI[2];
                } catch (Exception e) {
                    isFamilyAPI = true;
                }

                //authenticate
                Headers requestHeaders = exchange.getRequestHeaders();
                String authToken = requestHeaders.getFirst("Authorization");
                String userName = authenticate(authToken);

                //get event else get family events
                if(!isFamilyAPI){
                    //get event
                    EventService eventService = new EventService();
                    EventIDResult result = eventService.findEvent(eventID);

                    //verify that this eventID belongs to the auth token's user
                    if (!userName.equals(result.getAssociatedUsername())) {
                        throw new DataAccessException("This event does not belong to " + userName);
                    }

                    resultJson = gson.toJson(result);
                } else {
                    //get personID and family events
                    EventService eventService = new EventService();
                    EventResult result = eventService.findEvents(userName);

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