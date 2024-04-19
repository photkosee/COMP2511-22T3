package staff;

import java.time.LocalDate;

/**
 * A staff member
 * @author Phot Koseekrainiramon (z5387411)
 *
 */
public class StaffMember {

    protected String name = null;
    protected double salary = 0;
    protected LocalDate hireDate = java.time.LocalDate.now(), endDate = null;

    /**
     * Constructor for StaffMember
     * @param name
     * @param salary
     */
    public StaffMember(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    /**
     * Constructor for StaffMember
     * @param name
     * @param salary
     * @param date
     */
    public StaffMember(String name, double salary, LocalDate date) {
        this.name = name;
        this.salary = salary;
        this.endDate = date;
    }

    /**
     * Getter for name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for salary
     */
    public double getSalary() {
        return salary;
    }

    /**
     * Getter for hire date
     */
    public LocalDate getHireDate() {
        return hireDate;
    }

    /**
     * Getter for end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Setter for name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for salary
     * @param salary
     */
    public void setSalary(double salary) {
        this.salary = salary;
    }

    /**
     * Setter for hire date
     * @param date
     */
    public void setHireDate(LocalDate date) {
        this.hireDate = date;
    }

    /**
     * Setter for end date
     * @param endDate
     */
    public void setPosition(LocalDate date) {
        this.endDate = date;
    }

    @Override
    public String toString() {
        String msg = "Name: " + this.name + ", Salary: " + this.salary +
        ", Hire date: " + this.hireDate + ", End date: " + this.endDate;
        return msg;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) { return false; }
        if (obj == this) { return true; }

        if (this.getClass() != obj.getClass()) {
            return false;
        }

        StaffMember other = (StaffMember) obj;
        if (this.name == other.name && this.salary == other.salary &&
            this.hireDate == other.hireDate && this.endDate == other.endDate) {
            return true;
        } else {
            return false;
        }
    }
    
}
