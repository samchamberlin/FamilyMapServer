package model;

/** The User class creates a POJO storing user information */
public class User {

    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private char gender;
    private String personID;

    /** User constructor when all the user data is given */
    public User(String userName, String password, String email, String firstName, String lastName, char gender, String personID){
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    /** User constructor when nothing is given */
    public User(){}

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof User) {
            User u = (User) o;
            return u.getUsername().equals(getUsername()) &&
                    u.getPassword().equals(getPassword()) &&
                    u.getEmail().equals(getEmail()) &&
                    u.getFirstName().equals(getFirstName()) &&
                    u.getLastName().equals(getLastName()) &&
                    u.getGender() == getGender() &&
                    u.getPersonID().equals(getPersonID());
        } else {
            return false;
        }
    }
}
