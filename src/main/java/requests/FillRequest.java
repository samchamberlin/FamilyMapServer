package requests;

/** The FillRequest class creates a request for the FillService class */
public class FillRequest {

    private String userName;
    private int numGenerations;

    /** FillRequest constructor when all the fill data is given */
    public FillRequest(String userName, int numGenerations){
        this.userName = userName;
        this.numGenerations = numGenerations;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public int getNumGenerations() {
        return numGenerations;
    }

    public void setNumGenerations(int numGenerations) {
        this.numGenerations = numGenerations;
    }
}
