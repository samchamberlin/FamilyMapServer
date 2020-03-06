package handler;

import java.io.*;
import java.net.*;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import dao.DataAccessException;
import requests.RegisterRequest;
import results.LoginResult;
import results.Result;
import services.RegisterService;

/*
	The RegisterHandler is the HTTP handler that processes
	incoming HTTP requests that contain the "/user/register" URL path.

	Notice that RegisterHandler implements the HttpHandler interface,
	which is define by Java.  This interface contains only one method
	named "handle".  When the HttpServer object (declared in the Server class)
	receives a request containing the "/user/register" URL path, it calls
	RegisterHandler.handle() which actually processes the request.
*/
public class RegisterHandler implements HttpHandler {

    private Logger logger;
    Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("Register Handler");

        boolean success = false;

        try {

            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                //Load request, services, and result
                InputStream requestBody = exchange.getRequestBody();
                RegisterRequest request = gson.fromJson(readString(requestBody), RegisterRequest.class);
                RegisterService registerService = new RegisterService();
                LoginResult result = registerService.register(request);

                //Write result
                String resultJson = gson.toJson(result);
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
        The readString method shows how to read a String from an InputStream.
    */
    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    /*
    The writeString method shows how to write a String to an OutputStream.
*/
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

//    private boolean authTokenIsValid(String token) throws DataAccessException {
//        Database db = new Database();
//        Connection conn = db.openConnection();
//        AuthTokenDao aDao = new AuthTokenDao(conn);
//        if(aDao.find(token) != null){
//            return true;
//        } else {
//            return false;
//        }
//    }
}
