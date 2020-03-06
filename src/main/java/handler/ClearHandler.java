package handler;

import java.io.*;
import java.net.*;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;
import dao.DataAccessException;
import results.Result;
import services.ClearService;

public class ClearHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("Clear Handler");

        boolean success = false;
        Gson gson = new Gson();

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                ClearService cs = new ClearService();
                Result result = cs.clear();
                String resultJson = gson.toJson(result);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                OutputStream respBody = exchange.getResponseBody();
                writeString(resultJson, respBody);
                respBody.close();
                success = true;
            }

            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                OutputStream respBody = exchange.getResponseBody();
                Result errorResult = new Result("Clear failed due to a bad request.", false);
                String errorResultJson = gson.toJson(errorResult);
                writeString(errorResultJson, respBody);
                respBody.close();
            }
        }
        catch (IOException | DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            OutputStream respBody = exchange.getResponseBody();
            Result errorResult = new Result("Clear failed due to an internal error.", false);
            String errorResultJson = gson.toJson(errorResult);
            writeString(errorResultJson, respBody);
            respBody.close();
            e.printStackTrace();
        }
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}

