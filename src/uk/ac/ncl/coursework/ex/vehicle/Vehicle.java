package uk.ac.ncl.coursework.ex.vehicle;

/**
 * uk.ac.ncl.coursework.ex.vehicle.Vehicle - interface to a vehicle.
 *
 * @author Rouaa Yassin Kassab
 * @author Yu Hsuan Han - 18, FEBURUARY, 2026
 * Copyright (C) 2026 Newcastle University, UK
 */

/**
 * Represents a vehicle within the Vehicle Hire Management System.
 *
 * A Vehicle provides common behaviour required by all vehicle types
 * (e.g. cars and vans), including access to its unique identifier,
 * hire status, service requirements and mileage tracking.
 *
 * Implementations must ensure that service rules and inspection
 * requirements (where applicable) are respected.
 */
public interface Vehicle {
	//DO NOT remove or modify the signature of any existing method.
	//You can add additional methods (e.g. setter methods) if your solution requires that

	/**
	 * Returns the unique ID of the vehicle.
	 * All Vehicles must have an ID.
     *
	 * @return the uk.ac.ncl.coursework.ex.vehicle.VehicleID object
	 */
	VehicleID getVehicleID();


	/**
	 * Returns the uk.ac.ncl.coursework.ex.vehicle.Vehicle type.
	 * a uk.ac.ncl.coursework.ex.vehicle.Vehicle can be either a car or a van.
     *
	 * @return a string representing the vehicle type ("car" or "van")
	 */
	String getVehicleType();


	/**
	 * Indicates whether the vehicle is currently hired or not.
     *
	 * @return true if the uk.ac.ncl.coursework.ex.vehicle.Vehicle is hired; false otherwise
	 */
	boolean isHired();


	/**
	 * Returns the distance the vehicle must travel before it needs a service
     *
	 * @return an integer (the service distance requirement)
	 */
	public int getDistanceRequirement() ;


	/**
	 * Returns the distance a vehicle has traveled since the last service
     *
	 * @return an integer (the current mileage since the last service)
	 */
	public int getCurrentMileage(); 

	/**
	 * set the current mileage of the vehicle
     *
	 * @param mileage new mileage value
     * @throws IllegalArgumentException if mileage is negative
	 */
	public void setCurrentMileage(int mileage); 


	/**
	 * Checks whether the vehicle requires a service and performs the service if due.
	 * If the required distance has been reached or exceeded,
     * the mileage is reset and the method returns true.
     * Otherwise, no action is taken.
     *
	 * @return true if the uk.ac.ncl.coursework.ex.vehicle.Vehicle required a service and it was performed; false otherwise
	 */
	public boolean performServiceIfDue();


    /**
     * Indicates whether a van requires inspection.
     *
     * @return true if the vehicle requires an inspection; false otherwise.
     */
    public boolean requiresInspection();
}


