package uk.ac.ncl.coursework.ex.vehicle;

/**
 * Represents an immutable customer name and class.
 *
 * A Name consists of a first name and a last name.
 */

public final class Name {
    private final String firstName;
    private final String lastName;

    public Name(String firstName, String lastName) {
        if (firstName == null)
            throw new IllegalArgumentException("firstName is null");
        if (lastName == null)
            throw new IllegalArgumentException("lastName is null");
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * @return the first name
     */
    public String getFirstName(){

        return firstName;
    }

    /**
     * @return the last name
     */
    public String getLastName(){

        return lastName;
    }

    /**
     * Compares this Name with another object for equality.
     *
     * @param o the object to compare with
     * @return true if the names are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if(!(o instanceof Name))
            return false;
        final Name name = (Name)o;
        return firstName.equals(name.firstName) && lastName.equals(name.lastName);
    }

    /**
     * Returns a hash code consistent with equals.
     *
     * @return hash code value
     */
    @Override
    public int hashCode() {
        int hc = 17;
        hc = 37 * hc + firstName.hashCode();
        return 37 * hc + lastName.hashCode();
    }

    /**
     * Returns the full name as a string.
     *
     * @return first name and last name
     */
    @Override
    public String toString() {

        return "Name: "  + firstName + " " + lastName ;
    }
}
