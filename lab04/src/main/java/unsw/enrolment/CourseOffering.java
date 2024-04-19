package unsw.enrolment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import unsw.enrolment.exceptions.InvalidEnrolmentException;

public class CourseOffering extends Course {

    private Course course;
    private String term;
    private List<Enrolment> enrolments = new ArrayList<Enrolment>();

    public CourseOffering(Course course, String term) {
        super(course.getCode(), course.getTitle());
        this.course = course;
        this.term = term;
        this.course.addOffering(this);
    }

    public Course getCourse() {
        return course;
    }

    public String getCourseCode() {
        return course.getCode();
    }

    public List<Course> getCoursePrereqs() {
        return course.getPrereqs();
    }

    public String getTerm() {
        return term;
    }

    public Enrolment addEnrolment(Student student) throws InvalidEnrolmentException {
        if (checkValidEnrolment(student)) {
            Enrolment enrolment = new Enrolment(this, student);
            enrolments.add(enrolment);
            student.addEnrolment(enrolment);
            return enrolment;
        } else {
            throw new InvalidEnrolmentException("student has not satisfied the prerequisites");
        }
    }

    private boolean checkValidEnrolment(Student student) {
        for (Course prereq : getCoursePrereqs()) {
            if (!student.checkValidEnrolment(prereq)) {
                return false;
            }
        }

        return true;
    }

    public List<Student> studentsEnrolledInCourse() {
        List<Student> students = enrolments.stream().map(Enrolment::getStudent).collect(Collectors.toList());
        /*
        Comparator<Student> myCmp  = new Comparator<Student>() {

			@Override
			public int compare(Student o1, Student o2) {

				if (o1.getProgram() != o2.getProgram()) {
					return o1.getProgram() -  o2.getProgram(); 
				} else if (o1.getNumStreams() != o2.getNumStreams()) {
				    return o1.getNumStreams() -  o2.getNumStreams();
                } else if (!(o1.getName().equals(o2.getName()))) {
                    return o1.getName().compareTo(o2.getName());
                } else {
                    return o1.getId().compareTo(o2.getId());
                }
			}
		};
        students.sort(myCmp);
        */
        students.sort(Comparator.comparing(Student::getProgram)
                                .thenComparing(Student::getNumStreams)
                                .thenComparing(Student::getName)
                                .thenComparing(Student::getId));
        return students;
    }
}
