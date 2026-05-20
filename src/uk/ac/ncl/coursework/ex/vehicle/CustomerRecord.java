package uk.ac.ncl.coursework.ex.vehicle;

import java.util.Date;

/**
 * Represents an immutable customer record.
 *
 * A CustomerRecord stores:
 * Customer name
 * Date of birth
 * Licence type (commercial or standard)
 * Unique customer ID
 *
 * Customer records are immutable once created.
 */

public final class CustomerRecord {
    private final Name name;
    private final Date dob;
    private final boolean hasCommercialLicence;
    private final int customerID;

    /**
      * Constructs a new CustomerRecord.
      *
      * @param name customer's name
      * @param dob date of birth
      * @param hasCommercialLicence true if customer holds a commercial licence
      * @param customerID unique customer identifier
      *
      * @throws IllegalArgumentException if name or dob is null
      */

    public CustomerRecord(Name name, Date dob, boolean hasCommercialLicence, int customerID) {
        if(name == null)
            throw new IllegalArgumentException("Name is null");
        if(dob == null)
            throw new IllegalArgumentException("Date of birth is null");
        this.name = name;

        //Defensive copy to preserve immutability (Date is mutable)
        this.dob = new Date(dob.getTime());
        this.hasCommercialLicence = hasCommercialLicence;
        this.customerID = customerID;
    }

    /**
     * @return the customer's name
     */
    public Name getName() {

        return name;
    }

    /**
     * Returns a defensive copy of the customer's date of birth.
     *
     * @return a copy of the date of birth
     */
    public Date getDob() {

        return new Date(dob.getTime());
    }

    /**
     * @return true if the customer has a commercial licence, false otherwise
     */
    public boolean hasCommercialLicence() {

        return hasCommercialLicence;
    }

    /**
     * Returns the unique customer ID.
     *
     * @return the customer ID
     */
    public int getCustomerID() {

        return customerID;
    }


    /**
      * the combination of firstName, lastName and dob are unique for each Customer.
      * If you are adding a customer with similar existing information,
      * the method will throw an exception.
      */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if(!(o instanceof CustomerRecord))
            return false;
        final CustomerRecord cr = (CustomerRecord) o;
        return
             name.equals(cr.name) && dob.equals(cr.dob);
    }

    /**
     * Returns a hash code consistent with equals.
     *
     * @return hash code value
     */
    @Override
    public int hashCode() {
    int hc = 17;
    hc = 37 * hc + name.hashCode();
    return 37 * hc + dob.hashCode();
    }

    /**
     * Returns a string representation of this customer.
     *
     * @return a human-readable description of the customer
     */
    @Override
    public String toString() {
        return "Customer Record: " + name +
                ", Date of birth: " + dob +
                ", CustomerID: " + customerID ;
    }
}
