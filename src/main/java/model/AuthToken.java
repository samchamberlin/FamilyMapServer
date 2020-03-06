package model;

import java.util.UUID;

/** The AuthToken class creates a POJO storing auth token information */
public class AuthToken {

    private String authToken;
    private String userName;

    /** AuthToken constructor when all the auth token data is given */
    public AuthToken(String authToken, String userName){
        this.authToken = authToken;
        this.userName = userName;
    }

    /** AuthToken constructor to generate random auth token */
    public AuthToken(String userName) {
        this.userName = userName;
        this.authToken = UUID.randomUUID().toString().substring(0, 8);
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof AuthToken) {
            AuthToken a = (AuthToken) o;
            return a.getUsername().equals(getUsername()) &&
                    a.getAuthToken().equals(getAuthToken());
        } else {
            return false;
        }
    }

}
