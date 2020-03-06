package results;

import model.Person;

/** The PersonIDResult class creates a result for the PersonIDService class */
public class PersonIDResult {

    private String associatedUsername;
    private String personID;
    private String firstName;
    private String lastName;
    private char gender;
    private String fatherID;
    private String motherID;
    private String spouseID;
    private String message;
    private boolean success;

    /** PersonIDResult constructor when unsuccessful */
    public PersonIDResult(String message){
        this.message = message;
        this.success = false;
    }


    /** PersonIDResult constructor when successful */
    public PersonIDResult(String associatedUsername, String personID, String firstName, String lastName,
                          char gender, String fatherID, String motherID, String spouseID){
        this.associatedUsername = associatedUsername;
        this. personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
        this.success = true;
    }

    public PersonIDResult(Person p){
        this.associatedUsername = p.getAssociatedUsername();
        this. personID = p.getPersonID();
        this.firstName = p.getFirstName();
        this.lastName = p.getLastName();
        this.gender = p.getGender();
        this.fatherID = p.getFatherID();
        this.motherID = p.getMotherID();
        this.spouseID = p.getSpouseID();
        this.success = true;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
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

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
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
