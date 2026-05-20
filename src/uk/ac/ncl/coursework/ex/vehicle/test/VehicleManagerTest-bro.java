package uk.ac.ncl.coursework.ex.vehicle.test;

import uk.ac.ncl.coursework.ex.vehicle.*;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;


public class VehicleManagerTest{

        public static void main(String[] args) {
            VehicleManagerTest t = new VehicleManagerTest();

            System.out.println("Test add vehicle & no. of available vehicles");
            t.addVehicleAndAvailability();

            System.out.println("Test addCustomer record uniqueness");
            t.addCustomerUniqueness();

            System.out.println("Test hired vehicle rules");
            t.rulesOfHiredVehicles();

            System.out.println("Test vehicle return");
            t.returnVehicle();

            System.out.println("Test van inspection");
            t.vanInspection();

            System.out.println("Test service mileage");
            t.serviceMileage();

            System.out.println("Test unmodifiable");
            t.getVehiclesByCustomerUnmodifiable();

            System.out.println("Test exceptions");
            t.exceptions();
        }

        private void addVehicleAndAvailability() {

            VehicleManager vm = new VehicleManager();

            // ----- Normal case: empty manager
            Assertions.assertEquals(0, vm.noOfAvailableVehicles("car"));
            Assertions.assertEquals(0, vm.noOfAvailableVehicles("van"));

            // ----- Normal case: add vehicles and verify counts
            vm.addVehicle("car");
            vm.addVehicle("car");
            vm.addVehicle("van");

            Assertions.assertEquals(2, vm.noOfAvailableVehicles("car"));
            Assertions.assertEquals(1, vm.noOfAvailableVehicles("van"));
        }

        private void addCustomerUniqueness() {

            VehicleManager vm = new VehicleManager();
            Date dob = makeDob(2018, Calendar.FEBRUARY, 15);
            //Add customer and verify stored by ID
            CustomerRecord a = vm.addCustomerRecord("Ruby", "Han", dob, false);
            Assertions.assertTrue(vm.getCustomers().containsKey(a.getCustomerID()));

            //Should be rejected if it has same first name, last name and dob
            try {
                vm.addCustomerRecord("Ruby", "Han", dob, true);
                Assertions.assertNotReached();
            } catch (Throwable t) {
                Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
            }
        }

        private void rulesOfHiredVehicles() {

            VehicleManager vm = new VehicleManager();
            //Set up vehicles
            vm.addVehicle("car");
            vm.addVehicle("car");
            vm.addVehicle("car");
            vm.addVehicle("car");
            vm.addVehicle("van");

            //Set up customers
            CustomerRecord under18 = vm.addCustomerRecord("Ruby", "Han",
                    makeDob(2020, Calendar.NOVEMBER, 25), false);
            CustomerRecord noCommercialLicence = vm.addCustomerRecord("Nelson", "Hon",
                    makeDob(1998, Calendar.OCTOBER, 31), false);
            CustomerRecord hasCommercial = vm.addCustomerRecord("Uno", "Lam",
                    makeDob(1997, Calendar.MAY, 10), true);

            // ----- Normal case: hired car successfully
            Assertions.assertTrue(vm.hireVehicle(noCommercialLicence, "car", 1));
            Assertions.assertEquals(1, vm.getVechilesByCustomer(noCommercialLicence).size());
            Assertions.assertEquals(3, vm.noOfAvailableVehicles("car"));
            Assertions.assertEquals(1, vm.noOfAvailableVehicles("van"));

            // ----- Boundary case: under 18 cannot hire car
            Assertions.assertFalse(vm.hireVehicle(under18, "car", 1));

            // ----- Normal case: commercial adult hires van successfully
            Assertions.assertTrue(vm.hireVehicle(hasCommercial, "van", 1));
            Assertions.assertEquals(1, vm.getVechilesByCustomer(hasCommercial).size());
            Assertions.assertEquals(0, vm.noOfAvailableVehicles("van"));

            // ----- Boundary case: van requires a commercial licence
            VehicleManager vmv = new VehicleManager();

            vmv.addVehicle("van");
            CustomerRecord crv = vmv.addCustomerRecord("Ruby", "Hon",
                    makeDob(1994, Calendar.NOVEMBER, 25), false);

            Assertions.assertFalse(vmv.hireVehicle(crv, "van", 1));

            // ----- Boundary case: cannot hire more than 3 vehicles in total
            Assertions.assertTrue(vm.hireVehicle(hasCommercial, "car", 1));
            Assertions.assertTrue(vm.hireVehicle(hasCommercial, "car", 1));
            Assertions.assertFalse(vm.hireVehicle(hasCommercial, "car", 1));


            //Cannot hire until one returned
            VehicleManager vm1 = new VehicleManager();

            vm1.addVehicle("car"); // only 1 car

            CustomerRecord a = vm1.addCustomerRecord("Alice", "Lam",
                    makeDob(1999, Calendar.MARCH, 19), false);
            CustomerRecord b = vm1.addCustomerRecord("Vincent", "Hon",
                    makeDob(1998, Calendar.JUNE, 2), false);

            Assertions.assertEquals(1, vm1.noOfAvailableVehicles("car"));
            Assertions.assertTrue(vm1.hireVehicle(a, "car", 3));
            Assertions.assertEquals(0, vm1.noOfAvailableVehicles("car"));

            //Cannot hire car because there is no car left
            Assertions.assertFalse(vm1.hireVehicle(b, "car", 1));

            Vehicle hiredCar = null;
            for (Vehicle v : vm1.getVechilesByCustomer(a)) {

                hiredCar = v; break;
            }
            Assertions.assertTrue(hiredCar != null);

            vm1.returnVehicle(hiredCar.getVehicleID(), a, 1);

            //Car is available again
            Assertions.assertEquals(1, vm1.noOfAvailableVehicles("car"));
            Assertions.assertTrue(vm1.hireVehicle(b, "car", 3));
            Assertions.assertEquals(0, vm1.noOfAvailableVehicles("car"));

            //A vehicle can be hired to only one customer at a same time
            VehicleManager vm2 = new VehicleManager();
            vm2.addVehicle("van"); // only 1 van

            CustomerRecord c = vm2.addCustomerRecord("Vera", "Smith",
                    makeDob(1990, Calendar.JANUARY, 15), true);
            CustomerRecord d = vm2.addCustomerRecord("Victor", "Smith",
                    makeDob(1991, Calendar.SEPTEMBER, 15), true);

            Assertions.assertTrue(vm2.hireVehicle(c, "van", 1));
            Assertions.assertEquals(1, vm2.getVechilesByCustomer(c).size());
            Assertions.assertEquals(0, vm2.noOfAvailableVehicles("van"));

            //Van is already hired
            Assertions.assertFalse(vm2.hireVehicle(d, "van", 1));
            Assertions.assertEquals(0, vm2.getVechilesByCustomer(d).size());


        }

    private void returnVehicle() {

        VehicleManager vm = new VehicleManager();
        //Set up car and customer
        vm.addVehicle("car");
        CustomerRecord c = vm.addCustomerRecord("Ruby", "Puffet",
                makeDob(2000, Calendar.SEPTEMBER, 9), false);

        // ----- normal case: hire then return
        Assertions.assertEquals(1, vm.noOfAvailableVehicles("car"));
        Assertions.assertTrue(vm.hireVehicle(c, "car", 1));
        Assertions.assertEquals(0, vm.noOfAvailableVehicles("car"));

        Vehicle hiredCar = null;
        for (Vehicle v : vm.getVechilesByCustomer(c)) {
            hiredCar = v;
            break;
        }

        //Ensure a vehicle was actually hired
        Assertions.assertTrue(hiredCar != null);
        Assertions.assertTrue(hiredCar.isHired());

        //Return vehicle
        vm.returnVehicle(hiredCar.getVehicleID(), c, 5);

        // ----- Normal case: availability restored after return and update record
        Assertions.assertEquals(1, vm.noOfAvailableVehicles("car"));
        Assertions.assertEquals(0, vm.getVechilesByCustomer(c).size());
        Assertions.assertFalse(hiredCar.isHired());
    }

    private void vanInspection() {
        VehicleManager vm = new VehicleManager();

        //Set up van and customer
        vm.addVehicle("van");
        CustomerRecord c = vm.addCustomerRecord("Dinosaur", "Smith",
                makeDob(1994, Calendar.JANUARY, 27), true);

        // ----- Boundary case: duration >= 10 triggers inspection flag
        Assertions.assertTrue(vm.hireVehicle(c, "van", 10));


        Vehicle hiredVan = null;

        for (Vehicle v : vm.getVechilesByCustomer(c)) {
            hiredVan = v;
            break;  //Take the first hired vehicle
        }

        //Ensure a vehicle was actually hired
        Assertions.assertTrue(hiredVan != null);

        //Check inspection flag is set
        Assertions.assertTrue(hiredVan.requiresInspection());


        VehicleManager vm1 = new VehicleManager();

        vm1.addVehicle("van");

        CustomerRecord c1 = vm1.addCustomerRecord("Kiwi", "Fruit",
                makeDob(1993, Calendar.APRIL, 16), true);

        // ---- Normal case: duration < 10 does not trigger inspection
        Assertions.assertTrue(vm1.hireVehicle(c1, "van", 9));

        Vehicle hiredVan9 = null;
        for (Vehicle v : vm1.getVechilesByCustomer(c1)) {
            hiredVan9 = v;
            break;
        }
        Assertions.assertTrue(hiredVan9 != null);

        Assertions.assertFalse(hiredVan9.requiresInspection());

        //If the hire duration reaches or exceeds 10 days, the vehicle is marked for inspection.
        //Once the inspection is completed, the flag is reset,
        //and the van can be hire again
        VehicleManager vm2 = new VehicleManager();

        vm2.addVehicle("van");

        CustomerRecord a = vm2.addCustomerRecord("Lea", "Smith",
                makeDob(1995, Calendar.DECEMBER, 25), true);

        //Duration >= 10, requires inspection
        Assertions.assertTrue(vm2.hireVehicle(a, "van", 10));

        Vehicle hired = null;

        for (Vehicle v : vm2.getVechilesByCustomer(a)) {

            hired = v; break;

        }
        Assertions.assertTrue(hired != null);
        Assertions.assertTrue(hired.requiresInspection());

        //Return and conducts inspection if required
        vm2.returnVehicle(hired.getVehicleID(), a, 1);

        //Inspection should now be completed after return
        Assertions.assertFalse(hired.requiresInspection());

        //Available for hire again
        Assertions.assertTrue(vm2.hireVehicle(a, "van", 1));
    }

    private void serviceMileage() {
        // ----- Car boundary: mileage >= 10000 cannot be hired
        VehicleManager car = new VehicleManager();
        Vehicle v = car.addVehicle("car");
        v.setCurrentMileage(10000);

        CustomerRecord cr = car.addCustomerRecord(
                "McQueen", "Smith",
                makeDob(1990, Calendar.JULY, 7),
                false);

        Assertions.assertFalse(car.hireVehicle(cr, "car", 1));

        //Mileage < 10000 can be hired
        Vehicle v1 = car.addVehicle("car");
        v1.setCurrentMileage(9999);

        Assertions.assertTrue(car.hireVehicle(cr, "car", 1));


        // ----- Boundary case: mileage >= 5000 cannot be hired(van)
        VehicleManager van = new VehicleManager();
        Vehicle v2 = van.addVehicle("van");
        v2.setCurrentMileage(5000);

        CustomerRecord cr1 = van.addCustomerRecord(
                "Princess", "Pancake",
                makeDob(1990, Calendar.JULY, 7),
                true);

        Assertions.assertFalse(van.hireVehicle(cr1, "van", 1));

        //Mileage < 5000 can be hired
        Vehicle v3 = van.addVehicle("van");
        v3.setCurrentMileage(4999);

        Assertions.assertTrue(van.hireVehicle(cr1, "van", 1));
    }


    private void getVehiclesByCustomerUnmodifiable() {

            VehicleManager vm = new VehicleManager();
            vm.addVehicle("car");
            CustomerRecord c = vm.addCustomerRecord("Lion", "Yogurt",
                    makeDob(1990, Calendar.NOVEMBER, 20), true);
            Assertions.assertTrue(vm.hireVehicle(c, "car", 1));

            //Returned collection must be unmodifiable
            Collection<Vehicle> hired = vm.getVechilesByCustomer(c);
            try {
                hired.clear();
                Assertions.assertNotReached();
            } catch (Throwable t) {
                Assertions.assertExpectedThrowable(UnsupportedOperationException.class, t);
            }
        }
    private void exceptions() {
            VehicleManager vm = new VehicleManager();

            // addVehicle null
            try {
                vm.addVehicle(null);
                Assertions.assertNotReached();
            } catch (Throwable t) {
                Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
            }

            // noOfAvailableVehicles invalid type
            try {
                vm.noOfAvailableVehicles("ferry");
                Assertions.assertNotReached();
            } catch (Throwable t) {
                Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
            }

            // addCustomerRecord and hasCommercialLicense null
            try {
                vm.addCustomerRecord("Piggy", "Bo",
                        makeDob(2003, Calendar.MARCH, 14), null);
                Assertions.assertNotReached();
            } catch (Throwable t) {
                Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
            }

            // hireVehicle duration <= 0
            CustomerRecord c = vm.addCustomerRecord("Piggy", "Booo",
                       makeDob(2003, Calendar.MARCH, 14), false);
            try {
                vm.hireVehicle(c, "car", 0);
                Assertions.assertNotReached();
            } catch (Throwable t) {
                Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
            }

            // returnVehicle negative mileage
            try {
                vm.returnVehicle(VehicleID.generateCarID(), c, -1);
                Assertions.assertNotReached();
            } catch (Throwable t) {
                Assertions.assertExpectedThrowable(IllegalArgumentException.class, t);
            }
        }

   private static Date makeDob(int year, int month, int day) {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.set(year, month, day);
            return cal.getTime();
        }
    }

