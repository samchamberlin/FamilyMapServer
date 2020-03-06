package requests;

/** The LoginRequest class creates a request for the LoginService class */
public class LoginRequest {

    private String userName;
    private String password;

    /** LoginRequest constructor when all the login data is given */
    public LoginRequest(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
