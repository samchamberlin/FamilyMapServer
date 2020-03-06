package handler;

import java.io.*;
import java.net.*;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import dao.DataAccessException;
import requests.LoginRequest;
import results.LoginResult;
import results.Result;
import services.LoginService;

public class LoginHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("Login Handler");

        boolean success = false;
        Gson gson = new Gson();

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                //Load request, services, and result
                InputStream requestBody = exchange.getRequestBody();
                System.out.println(exchange.getRequestURI().toString());
                System.out.println(requestBody);
                LoginRequest request = gson.fromJson(readString(requestBody), LoginRequest.class);
                LoginService loginService = new LoginService();
                LoginResult result = loginService.login(request);

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
        The writeString method shows how to write a String to an OutputStream.
    */
    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
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
}
