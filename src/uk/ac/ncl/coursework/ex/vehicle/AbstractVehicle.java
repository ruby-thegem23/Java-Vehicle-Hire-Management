package uk.ac.ncl.coursework.ex.vehicle;

/**
 * Abstract base implementation of the Vehicle interface.
 *
 * Provides the shared information and common functions for all vehicles,
 * such as rental status and mileage tracking.
 *
 * Specific vehicle types Car and Van will extend this class to implement
 * their own service distance and inspection.
 */

public abstract class AbstractVehicle implements Vehicle {

    public static final String CAR = "car";
    public static final String VAN = "van";

    private final VehicleID id;
    private boolean isHired;
    private int currentMileage;

    AbstractVehicle(VehicleID id) {
        if (id == null)
            throw new IllegalArgumentException("uk.ac.ncl.coursework.ex.vehicle.Vehicle ID is null");
        this.id = id;
        this.isHired = false;
        this.currentMileage = 0;
    }

    /**
      * Factory method for creating a vehicle of the specified type.
      *
      * @param vehicleType type of vehicle ("car" or "van")
      * @param id unique vehicle identifier
      * @return a new Vehicle instance
      * @throws IllegalArgumentException if vehicleType is invalid
      */

    public static Vehicle getInstance(String vehicleType, VehicleID id) {

        if (vehicleType == null)
            throw new IllegalArgumentException("Vehicle type is null");

        if (id == null)
            throw new IllegalArgumentException("VehicleID is null");

        if (vehicleType.equalsIgnoreCase(CAR)) {
            return new Car(id);
        }
        else if (vehicleType.equalsIgnoreCase(VAN)) {
            return new Van(id);
        }
        else {
            throw new IllegalArgumentException("Invalid vehicle type");
        }
    }


    final void setHired(boolean hired) {
        this.isHired = hired;
    }

    @Override
    public final VehicleID getVehicleID(){
        return id;
    }
    @Override
    public final boolean isHired(){
        return isHired;
    }
    @Override
    public final int getCurrentMileage(){
        return currentMileage;
    }

    /**
     * Sets the current mileage of this vehicle.
     *
     * @param mileage the new mileage value
     */

    @Override
    public final void setCurrentMileage(int mileage){
        if(mileage < 0)
            throw new IllegalArgumentException("Current mileage is negative");
        this.currentMileage = mileage;
    }

    /**
     * Performs routine service if the vehicle has reached
     * its service distance requirement.
     *
     * @return true if service was performed, false otherwise
     */
    @Override
    public final boolean performServiceIfDue(){

        //Service is modelled as resetting mileage to 0 once threshold is reached
        if(currentMileage >= getDistanceRequirement()){
            currentMileage = 0;
            return true;
        }
        return false;
    }

    /**
     * Returns a string representation of this vehicle.
     *
     * @return a human-readable summary of the vehicle
     */
    @Override
    public String toString() {
        return "Type: " + getVehicleType() +
                ", ID: " + id +
                ", Mileage: " + currentMileage +
                ", Hired: " + isHired;
    }

}