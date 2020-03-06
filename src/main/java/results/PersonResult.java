package results;

import model.Person;

import java.util.ArrayList;

/** The PersonResult class creates a result for the PersonService class */
public class PersonResult {

    private ArrayList<Person> data;
    private String message;
    private boolean success;

    /** PersonResult constructor for single person
     * @param message The failed message
     */
    public PersonResult(String message){
        this.message = message;
        success = false;
    }


    /** PersonResult constructor for family
     * @param persons All the family members of the user
     */
    public PersonResult(ArrayList<Person> persons){
        this.data = persons;
        success = true;
    }

    public ArrayList<Person> getData() {
        return data;
    }

    public void setData(ArrayList<Person> data) {
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
