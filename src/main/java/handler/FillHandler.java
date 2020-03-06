package handler;

import java.io.*;
import java.net.*;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import dao.DataAccessException;
import requests.FillRequest;
import results.Result;
import services.FillService;

public class FillHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("Fill Handler");

        boolean success = false;
        Gson gson = new Gson();

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                //Get userName
                String path = exchange.getRequestURI().getPath();
                String[] URI = path.split("/");
                String userName = URI[2];

                //Get num generations
                int numGenerations;
                try{
                    numGenerations = Integer.parseInt(URI[URI.length - 1]);
                } catch (Exception e) {
                    numGenerations = 4;
                }

                //Fill request, services, and result
                FillRequest fillRequest = new FillRequest(userName, numGenerations);
                FillService fillService = new FillService();
                Result result = fillService.fill(fillRequest);

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
}