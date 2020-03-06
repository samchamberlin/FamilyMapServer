package results;

/** The Result class creates a result for basic Service classes */
public class Result {

    private String message;
    private boolean success;

    /** Result constructor */
    public Result(String message, boolean success){
        this.message = message;
        this.success = success;
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
