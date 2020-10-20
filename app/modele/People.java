package modele;

public class People {

    public static final String COLLECTION_NAME = "people";
    public static final String CLASS_NAME = "_t";
    
    public static class Schema {
        public static final String lastName = "lastName";
        public static final String firstName = "firstName";
    }
    
    private String firstName;
    
    private String lastName;

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
}
