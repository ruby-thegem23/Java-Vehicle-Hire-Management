package uk.ac.ncl.coursework.ex.vehicle.test;

import uk.ac.ncl.coursework.ex.vehicle.Assertions;
import uk.ac.ncl.coursework.ex.vehicle.CustomerRecord;
import uk.ac.ncl.coursework.ex.vehicle.Name;

import java.util.Calendar;
import java.util.Date;

public class CustomerRecordTest {

    public static void main(String[] args) {
        CustomerRecordTest t = new CustomerRecordTest();

        System.out.println("Test equals & hashCode");
        t.equalsAndHashCode();

        System.out.println("Test defensive copying");
        t.defensiveCopying();

        System.out.println("Test exceptions");
        t.exceptions();

    }
    private void equalsAndHashCode() {

        // ----- Normal case
        Date dob = makeDob(2026, Calendar.FEBRUARY, 9);

        CustomerRecord cr1 = new CustomerRecord(new Name("Ruby", "Han"), dob,
                false, 7);
        CustomerRecord cr2 = new CustomerRecord(new Name("Ruby", "Han"), dob,
                true,  789);
        CustomerRecord cr3 = new CustomerRecord(new Name("Ruby", "Han"),
                makeDob(2026, Calendar.NOVEMBER, 9),
                false, 890);
        Assertions.assertTrue(cr1.equals(cr2));
        Assertions.assertTrue(cr2.equals(cr1));
        Assertions.assertEquals(cr1.hashCode(), cr2.hashCode());

        // ----- Boundary case
        Assertions.assertFalse(cr1.equals(cr3));
        Assertions.assertNotEquals(cr1, cr3);
    }
    private void defensiveCopying(){

        // ----- Defensive copying (Date is mutable)
        Date external = makeDob(1995, Calendar.NOVEMBER, 25);
        CustomerRecord cr4 = new CustomerRecord(new Name("Ruby", "Han"), external,
                false, 5);
        Date returndob = cr4.getDob();

        //Ensure CustomerRecord remains unchanged
        external.setTime(0);
        returndob.setTime(0);
        Assertions.assertFalse(cr4.getDob().equals(returndob));
    }
    private void exceptions(){
        //Name cannot be null
        try {
            new CustomerRecord(null, makeDob(2026, Calendar.FEBRUARY, 9),
                    true, 7);
            Assertions.assertNotReached();
        } catch (Throwable t) {
            Assertions.assertExpectedThrowable(
                    IllegalArgumentException.class, t);
        }
        //Date of birth cannot be null
        try {
            new CustomerRecord(new Name("Ruby", "Han"), null,
                    false, 543);
            Assertions.assertNotReached();
        } catch (Throwable t) {
            Assertions.assertExpectedThrowable(
                    IllegalArgumentException.class, t);
        }
    }
    private static Date makeDob(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month, day);
        return cal.getTime();
    }
}

