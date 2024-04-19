package staff;

import java.time.LocalDate;

/**
 * Lecturer
 * @author Phot Koseekrainiramon (z5387411)
 *
 */
public class Lecturer extends StaffMember {

    private String school = null, academicStatus = null;

    /**
     * Constructor for Lecturer
     * @param name
     * @param salary
     * @param school
     * @param academicStatus
     */
    public Lecturer(String name, double salary, String school, String academicStatus) {
        super(name, salary);
        this.school = school;
        this.academicStatus = academicStatus;
    }

    /**
     * Constructor for StaffMember
     * @param name
     * @param salary
     * @param date
     * @param school
     * @param academicStatus
     */
    public Lecturer(String name, double salary, LocalDate date, String school, String academicStatus) {
        super(name, salary, date);
        this.school = school;
        this.academicStatus = academicStatus;
    }

    /**
     * Getter for school name
     */
    public String getSchool() {
        return school;
    }

    /**
     * Getter for academic status
     */
    public String getAcademicStatus() {
        return academicStatus;
    }

    /**
     * Setter for school name
     * @param school
     */
    public void setSchool(String school) {
        this.school = school;
    }

    /**
     * Setter for academic status
     * @param academicStatus
     */
    public void setAcademicStatus(String academicStatus) {
        this.academicStatus = academicStatus;
    }

    @Override
    public String toString() {
        String msg = "Name: " + name + ", Salary: " + salary +
            ", Hire date: " + hireDate + ", End date: " + endDate +
            ", School name: " + school + ", Academic status: " + academicStatus;
        return msg;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj) == false) { return false; }

        Lecturer other = (Lecturer) obj;
        if (this.school == other.school &&
            this.academicStatus == other.academicStatus) {
            return true;
        } else {
            return false;
        }
    }
}
