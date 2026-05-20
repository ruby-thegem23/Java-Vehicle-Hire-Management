package uk.ac.ncl.coursework.ex.vehicle;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a unique vehicle identifier.
 *
 * A VehicleID consists of:
 * A three-character prefix (vehicle type, random letter, random digit)
 * A three-digit number separated by a hyphen.
 *
 * Car IDs must have an even three-digit number.
 * Van IDs must have an odd three-digit number.
 *
 * VehicleIDs are guaranteed to be unique.
 */

public final class VehicleID {

    private final char vehicleType;
    private final char letter;
    private final int digit;
    private final int number;

    //Tracks all issued VehicleID to guarantee uniqueness
    private static final Set<VehicleID> alreadyHave = new HashSet<>();

    private VehicleID(char vehicleType, char letter, int digit, int number){
        if (vehicleType != 'C' && vehicleType != 'V') {
            throw new IllegalArgumentException("VehicleType must be 'C' or 'V'");
        }

        if (letter < 'A' || letter > 'Z') {
            throw new IllegalArgumentException("Letter must be A..Z");
        }

        if (digit < 0 || digit > 9) {
            throw new IllegalArgumentException("Digit must be 0 - 9");
        }

        if (number < 0 || number > 999) {
            throw new IllegalArgumentException("Number must be 0 - 999");
        }

        this.vehicleType = vehicleType;
        this.letter = letter;
        this.digit = digit;
        this.number = number;
    }

    /**
     * Returns the vehicle type character.
     *
     * @return 'C' for car or 'V' for van
     */
    public char getVehicleType() {

        return vehicleType;
    }

    /**
     * Returns the random letter component.
     *
     * @return the letter
     */
    public char getLetter() {

        return letter;
    }

    /**
     * Returns the random digit component.
     *
     * @return the digit
     */
    public int getDigit() {

        return digit;
    }

    /**
     * Returns the three-digit number component.
     *
     * @return the number
     */
    public int getNumber() {

        return number;
    }

    /**
      * Generates a unique VehicleID for a car.
      *
      * @return a new unique VehicleID
      */

    public static VehicleID generateCarID() {

        //Keep generating until a unique unused ID is found
        while (true) {
            char letter = (char) ('A' + (int) (Math.random() * 26));
            int digit = (int) (Math.random() * 10);
            int number = (int) (Math.random() * 1000);

            //Car number must be even
            if (number % 2 != 0) {
                if (number == 999) number--;
                else number++;
            }
            VehicleID candidate = new VehicleID('C', letter, digit, number);
            if (!alreadyHave.contains(candidate)) {
                alreadyHave.add(candidate);
                return candidate;
            }
        }
    }

    /**
      * Generates a unique VehicleID for a van.
      *
      * @return a new unique VehicleID
      */

    public static VehicleID generateVanID() {
        while (true) {
            char letter = (char) ('A' + (int) (Math.random() * 26));
            int digit = (int) (Math.random() * 10);
            int number = (int) (Math.random() * 1000);

            //Van number must be odd
            if (number % 2 == 0){
                number++;
            }

            VehicleID candidate = new VehicleID('V', letter, digit, number);
            if (!alreadyHave.contains(candidate)) {
                alreadyHave.add(candidate);
                return candidate;
            }
        }
    }

    /**
     * Compares this VehicleID with another object for equality.
     *
     * @param o the object to compare with
     * @return true if the VehicleIDs are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VehicleID)) return false;

        VehicleID other = (VehicleID) o;
        return vehicleType == other.vehicleType
                && letter == other.letter
                && digit == other.digit
                && number == other.number;
    }

    /**
     * Returns a hash code consistent with equals.
     *
     * @return hash code value
     */
    @Override
    public int hashCode() {
        int hc = 17;
        hc = 37 * hc + vehicleType;
        hc = 37 * hc + letter;
        hc = 37 * hc + digit;
        return 37 * hc + number;
    }

    /**
      * Returns a string representation of the VehicleID
      * in the format XXX-000.
      *
      * @return formatted VehicleID string
      */

    @Override
    public String toString() {
        return "" + vehicleType + letter + digit + "-" + String.format("%03d", number);
    }

}
