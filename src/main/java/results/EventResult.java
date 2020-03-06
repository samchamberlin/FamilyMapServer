package results;

import model.Event;

import java.util.ArrayList;

/** The EventResult class creates a result for the EventService class */
public class EventResult {

    private ArrayList<Event> data;
    private String message;
    private boolean success;

    /** EventResult constructor when not successful */
    public EventResult(String message){
        this.message = message;
        success = false;
    }

    /** EventResult constructor when successful */
    public EventResult(ArrayList<Event> events){
        this.data = events;
        success = true;
    }

    public ArrayList<Event> getData() {
        return data;
    }

    public void setData(ArrayList<Event> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
