package uk.ac.ncl.coursework.ex.vehicle;

/**
 * Represents a car in the Vehicle Hire Management System.
 *
 * Cars require servicing every 10,000 miles.
 * Cars do not require inspection.
 */

final class Car extends AbstractVehicle {
    private static final int serviceDistance = 10000;

    Car(VehicleID id)
    {
        super(id);
    }

    @Override
    public String getVehicleType(){

        return "car";
    }
    @Override
    public int getDistanceRequirement(){

        return serviceDistance;
    }
    @Override
    public boolean requiresInspection(){

        return false;
    }

}
