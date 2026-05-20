package uk.ac.ncl.coursework.ex.vehicle;

/**
 * Represents a van in the Vehicle Hire Management System.
 *
 * Vans require servicing every 5,000 miles.
 * Vans may require inspection depending on
 * hire duration and regulatory conditions.
 */

final class Van extends AbstractVehicle {
    private static final int serviceDistance = 5000;


    private boolean needsInspection;

    Van(VehicleID id) {
        super(id);
    }

    /**
     * Sets whether this van requires inspection.
     *
     * @param required true if inspection is required
     */

    public void setNeedsInspection(boolean required) {

        this.needsInspection = required;
    }



    @Override
    public String getVehicleType(){

        return "van";
    }
    @Override
    public int getDistanceRequirement(){

        return serviceDistance;
    }
    @Override
    public boolean requiresInspection(){

        return needsInspection;
    }
}