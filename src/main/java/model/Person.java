package model;

/** The Person class creates a POJO storing person information */
public class Person {

    private String personID;
    private String associatedUsername;
    private String firstName;
    private String lastName;
    private char gender;
    private String fatherID;
    private String motherID;
    private String spouseID;

    /** Person constructor when all the person data is given */
    public Person(String personID, String associatedUsername, String firstName, String lastName, char gender,
                  String fatherID, String motherID, String spouseID){
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    /** Person constructor when person object is given */
    public Person(Person p){
        this.personID = p.getPersonID();
        this.associatedUsername = p.getAssociatedUsername();
        this.firstName = p.getFirstName();
        this.lastName = p.getLastName();
        this.gender = p.getGender();
        this.fatherID = p.getFatherID();
        this.motherID = p.getMotherID();
        this.spouseID = p.getSpouseID();
    }

    /** Person constructor when nothing is given */
    public Person(){}

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
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

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof Person) {
            Person p = (Person) o;
            return p.personID.equals(getPersonID()) &&
                    p.getAssociatedUsername().equals(getAssociatedUsername()) &&
                    p.getFirstName().equals(getFirstName()) &&
                    p.getLastName().equals(getLastName()) &&
                    p.getGender() ==  getGender() &&
                    p.getFatherID().equals(getFatherID()) &&
                    p.getMotherID().equals(getMotherID()) &&
                    p.getSpouseID().equals(getSpouseID());
        } else {
            return false;
        }
    }
}
