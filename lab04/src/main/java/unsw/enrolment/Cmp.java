package unsw.enrolment;

import java.util.Comparator;

public class Cmp implements Comparator<Student>{

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
}

