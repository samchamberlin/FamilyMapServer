package results;

/** The RegisterResult class creates a result for the RegisterService class */
public class RegisterResult {

    private String authToken;
    private String userName;
    private String personID;
    private String message;
    private boolean success;

    /** RegisterResult constructor when login is not a success
     * @param message The failed message
     */
    public RegisterResult(String message){
        this.message = message;
        this.success = false;
    }

    /** RegisterResult constructor when login is a success
     * @param authToken The authToken of the user
     * @param userName The userName of the user
     * @param personID The personID of the user
     */
    public RegisterResult(String authToken, String userName, String personID){
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
        this.success = true;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
