package results;

/** The LoginResult class creates a result for the LoginService class */
public class LoginResult {

    private String authToken;
    private String userName;
    private String personID;
    private String message;
    private boolean success;

    /** LoginResult constructor when login is not a success */
    public LoginResult(String message){
        this.message = message;
        this.success = false;
    }

    /** LoginResult constructor when login is a success */
    public LoginResult(String authToken, String userName, String personID){
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
