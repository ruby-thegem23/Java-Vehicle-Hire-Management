package uk.ac.ncl.coursework.ex.vehicle;

import java.util.*;

/**
 * uk.ac.ncl.coursework.ex.vehicle.VehicleManager
 *
 * @author Rouaa Yassin Kassab
 * Copyright (C) 2026 Newcastle University, UK
 */

/**
 * Manages vehicles and customers.
 *
 * Vehicle manager class is responsible for:
 * Storing all vehicles
 * Maintaining customer records
 * Managing hire contracts
 * Enforcing rules for vehicle hire
 *
 * Vehicles may only be hired depends on
 * age, licence and service constraints.
 */

public class VehicleManager {

    //Map of all vehicles in the company's fleet
    //Key : VehicleID, Value : Vehicle instance
    private final Map<VehicleID, Vehicle> allVehicles = new HashMap<>();

    //Map of all registered customers
    //Key : customerID, Value : CustomerRecord
    private final Map<Integer, CustomerRecord> customers = new HashMap<>();

    //Tracks current hires
    //Key : customerID, Value :Set of vehicles currently hired by that customer
    private final Map<Integer, Set<Vehicle>> hiredVehcile = new HashMap<>();
    private int nextCustomerID = 1;

    /**
     * Returns a map of all vehicles managed by the system.
     *
     * @return a map from VehicleID to Vehicle
     */
    public Map<VehicleID, Vehicle> getAllVehicles() {

        return allVehicles;
    }

    /**
     * Returns a map of all registered customers.
     *
     * @return a map from customer ID to CustomerRecord
     */
    public Map<Integer, CustomerRecord> getCustomers() {

        return customers;
    }

    /**
     * Returns a map of currently hired vehicles grouped by customer.
     *
     * @return a map from customer ID to a set of hired vehicles
     */
    public Map<Integer, Set<Vehicle>> getHiredVehicles() {

        return hiredVehcile;
    }

    /**
      * Adds a new vehicle of the specified type to the system.
      *
      * @param vehicleType "car" or "van"
      * @return the newly created Vehicle
      * @throws IllegalArgumentException if vehicleType is null or invalid
      */

    public Vehicle addVehicle(String vehicleType){
        if (vehicleType == null) {
            throw new IllegalArgumentException(".Vehicle type is null");
        }

        VehicleID newVehicleID;

        //Generate a valid unique ID (car: even number, van: odd number)
        if(vehicleType.equalsIgnoreCase("car")) {
            newVehicleID = VehicleID.generateCarID();
        }else if(vehicleType.equalsIgnoreCase("van")){
            newVehicleID =VehicleID.generateVanID();
        }else{
            throw new IllegalArgumentException("Invalid vehicle type.");
        }
        //Factory method: create the correct concrete Vehicle (Car/Van) for the given type
        Vehicle newVehicle = AbstractVehicle.getInstance(vehicleType, newVehicleID);

        // Store vehicle in the fleet map
        allVehicles.put(newVehicleID, newVehicle);

        return newVehicle;
    }

    /**
      * Returns the number of available (not hired) vehicles
      * of the specified type currently registered in the system.
      *
      * @param vehicleType the type of vehicle ("car" or "van")
      * @return the number of vehicles of the given type that are not hired
      * @throws IllegalArgumentException if vehicleType is null or invalid
      */

    public int noOfAvailableVehicles(String vehicleType) {
        if (vehicleType == null) {
            throw new IllegalArgumentException("Vehicle type is null");
        }else if(!vehicleType.equalsIgnoreCase("car") &&
            !vehicleType.equalsIgnoreCase("van")){
            throw new IllegalArgumentException("Invalid vehicle type.");
        }

        int count = 0;

        for(Vehicle v : allVehicles.values()){
            if(v.getVehicleType().equalsIgnoreCase(vehicleType) && !v.isHired())
            count++;
        }
        return count;
    }

    /**
      * Creates and registers a new customer record.
      *
      * The combination of first name, last name and date of birth
      * must be unique. If a customer with identical details already
      * exists, an exception is thrown.
      *
      * @param firstName customer's first name
      * @param lastName customer's last name
      * @param dob customer's date of birth
      * @param hasCommercialLicense true if the customer holds a commercial licence
      * @return the newly created CustomerRecord
      * @throws IllegalArgumentException if the customer already exists
      * or if input is invalid
      */

    public CustomerRecord addCustomerRecord(String firstName, String lastName,
                                            Date dob, Boolean hasCommercialLicense){
        if(hasCommercialLicense == null){
            throw new IllegalArgumentException("hasCommercialLicense is null");
        }

        Name name = new Name(firstName, lastName);
        //Used only for duplicate detection (equals uses name + dob)
        CustomerRecord temp = new CustomerRecord(name, dob, hasCommercialLicense,0);

        for(CustomerRecord existing : customers.values()){
            if(existing.equals(temp)){
                throw new IllegalArgumentException("Customer record already exists!");
            }
        }

        //Allocate a new unique customer ID
        int id = nextCustomerID++;
        CustomerRecord newCr = new CustomerRecord(name, dob, hasCommercialLicense,id);

        customers.put(id, newCr);

        return newCr;
    }

    /**
      * Attempts to hire a vehicle of the specified type for a customer.
      *
      * This method guarantees that every rental meets the requirements:
      * Maximum of three hired vehicles per customer
      * Age restrictions (18 for cars, 23 for vans)
      * Commercial licence requirement for vans
      * Service and inspection constraints
      *
      * @param customerRecord the customer requesting hire
      * @param vehicleType type of vehicle
      * @param duration hire duration days
      * @return true if hire successful, false otherwise
      *
      * @throws IllegalArgumentException if parameters are invalid
      */

    public boolean hireVehicle(CustomerRecord customerRecord, String vehicleType,
                               int duration) {
        //Validation: null and invalid type
        if (customerRecord == null)
            throw new IllegalArgumentException("CustomerRecord is null");

        if (vehicleType == null)
            throw new IllegalArgumentException("Vehicle type is null");

        if (duration <= 0)
            throw new IllegalArgumentException("Invalid duration");

        if (!vehicleType.equalsIgnoreCase("car") &&
                !vehicleType.equalsIgnoreCase("van"))
            throw new IllegalArgumentException("Invalid vehicle type");

        if (!customers.containsKey(customerRecord.getCustomerID()))
            throw new IllegalArgumentException("Customer does not exist");

        int customerID = customerRecord.getCustomerID();

        //Rule 1: Customer cannot hired more than three vehicles
        if (hiredVehcile.containsKey(customerID) &&
                hiredVehcile.get(customerID).size() >= 3) {

            System.out.println("Sorry,you can not hired more than 3 vehicles in total.");
            return false;
        }

        Date dob = customerRecord.getDob();
        Calendar today = Calendar.getInstance();
        Calendar birth = Calendar.getInstance();
        birth.setTime(dob);

        int age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < birth.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        //Rule 2:Age and licence eligibility
        //Car:  <= age 18
        //Van:  <= 23 + commercial licence required
        if (vehicleType.equalsIgnoreCase("car")) {
            if (age < 18) {
                System.out.println("Sorry, must be at least 18 to hire a car.");
                return false;
            }
        } else if (vehicleType.equalsIgnoreCase("van")) {
            if (age < 23 || !customerRecord.hasCommercialLicence()) {
                System.out.println("Sorry, van hire requires age >= 23 and have a commercial licence.");
                return false;
            }
        }

        //Rule 3: Choose the first available vehicle that is not hired and not restricted
        Vehicle selectedVehicle = null;
        for (Vehicle v : allVehicles.values()) {
            if (v.getVehicleType().equalsIgnoreCase(vehicleType) && !v.isHired()) {
                //Skip vehicles already hired
                if (v.getCurrentMileage() >= v.getDistanceRequirement()) {
                    continue;
                }
                //Skip vehicles that have reached service distance requirement
                if (vehicleType.equalsIgnoreCase("van") && v.requiresInspection()) {
                    continue;
                }
                selectedVehicle = v;
                break;
            }
        }
            if (selectedVehicle == null) {
                System.out.println("No vehicle available.");
                return false;
            }
            //Safe cast: Vehicles are created by our factory as subclasses of AbstractVehicle
            ((AbstractVehicle) selectedVehicle).setHired(true);

            // If a van is hired for 10 days or more, mark it as needing inspection.
            if (vehicleType.equalsIgnoreCase("van") && duration >= 10) {
                ((Van) selectedVehicle).setNeedsInspection(true);
            }
            if (!hiredVehcile.containsKey(customerID)) {
                //Add this hire in the hired vehicles map.
                hiredVehcile.put(customerID, new HashSet<Vehicle>());
            }
                hiredVehcile.get(customerID).add(selectedVehicle);

                System.out.println("Congraduation! Customer: " + customerRecord.getName() + " is hiring " + selectedVehicle.getVehicleID());
                return true;
        }

    /**
      * Terminates a hire contract and returns the vehicle to the system.
      *
      * The method:
      * Removes the vehicle from the customer's hire record
      * Updates mileage
      * Performs servicing if due
      * Conducts inspection if required (for vans)
      *
      * If no such contract exists, no action is taken.
      *
      * @param vehicleID identifier of the returned vehicle
      * @param customerRecord customer returning the vehicle
      * @param mileage distance travelled during hire
      *
      * @throws IllegalArgumentException if parameters are invalid
      */

    public void returnVehicle(VehicleID vehicleID ,CustomerRecord customerRecord,
                              int mileage) {
        if (vehicleID == null)
            throw new IllegalArgumentException("VehicleID is null");

        if (customerRecord == null)
            throw new IllegalArgumentException("CustomerRecord is null");

        if (mileage < 0)
            throw new IllegalArgumentException("Mileage cannot be negative");

        int customerID = customerRecord.getCustomerID();

        //Retrieve vehicle from fleet to ensure consistency
        Vehicle companyVehicle = allVehicles.get(vehicleID);
        if (companyVehicle == null)
            return;

        //Get all vehicles hired by this customer
        Set<Vehicle> hired = hiredVehcile.get(customerID);
        if (hired == null)
            return;

        Vehicle returnedVehicle = null;

        for (Vehicle v : hired) {
            if (v.getVehicleID().equals(vehicleID)) {
                returnedVehicle = v;
                break;
            }
        }

        if (returnedVehicle == null)
            return;

        //Remove vehicle from customer's hire record
        hired.remove(returnedVehicle);

        //Clean up empty entries
        if (hired.isEmpty()) {
            hiredVehcile.remove(customerID);
        }


        ((AbstractVehicle) companyVehicle).setHired(false);

        //Update mileage after return
        companyVehicle.setCurrentMileage(
                companyVehicle.getCurrentMileage() + mileage
        );

        //Perform routine service if service distance has been reached
        companyVehicle.performServiceIfDue();

        //Clear inspection requirement upon return
        if (companyVehicle instanceof Van) {
            Van van = (Van) companyVehicle;
            if (van.requiresInspection()) {
                van.setNeedsInspection(false);
            }
        }


    }

    /**
      * Returns an unmodifiable collection of vehicles currently hired by the customer.
      *
      * @param customerRecord customer whose vehicles are requested
      * @return unmodifiable collection of hired vehicles (possibly empty)
      *
      * @throws IllegalArgumentException if customerRecord is null
      */

    public Collection<Vehicle> getVechilesByCustomer (CustomerRecord
                                                              customerRecord){
        if (customerRecord == null)
            throw new IllegalArgumentException("CustomerRecord is null");

        int customerID = customerRecord.getCustomerID();

        Set<Vehicle> hired = hiredVehcile.get(customerID);

        if (hired == null)
            return Collections.emptySet();

        return Collections.unmodifiableCollection(hired);
    }

}