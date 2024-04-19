package staff.test;

import java.time.LocalDate;

import staff.*;

public class StaffTest {
    public void printStaffDetails(StaffMember StaffMember) {
        System.out.println(StaffMember.toString());
    }

    public static void main(String[] args) {
        LocalDate date1 = LocalDate.of(2000, 4, 21);
        LocalDate date2 = LocalDate.of(2000, 5, 6);
        StaffMember A = new StaffMember("A", 500, date1);
        Lecturer B = new Lecturer("A", 500, date2, "UNSW", "Senior");
        StaffMember C = B;
        StaffMember D = new StaffMember("A", 500);
        Lecturer E = new Lecturer("A", 500, "UNSW", "Senior");

        System.out.println(A);
        System.out.println(E);
        System.out.println(A.equals(B));
        System.out.println(A.equals(D));
        System.out.println(C.equals(B));
        System.out.println(A.equals(C));
        System.out.println(E.equals(B));
    }
}
