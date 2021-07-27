package il.ac.mta.zuli.evolution.engine.data;

import il.ac.mta.zuli.evolution.engine.data.generated.ETTStudy;

import java.util.Map;

public class Requirement {
    //required hours per subject
    private int hours;
    private Subject subject;

    public Requirement(ETTStudy ettRequirement) {
        setHours(ettRequirement.getHours());
        setSubject(ettRequirement.getSubjectId());
        //after ctor add subject using timeTable's subject map
    }

    public int getHours() {
        return hours;
    }

    private void setHours(int hours) {
        if (hours > 0) {
            this.hours = hours;
        } else {//throw exception
            System.out.println("hours are 0 or negative");
        }
    }

    public Subject getSubject() {
        return subject;
    }

    private void setSubject(int id) {
        Descriptor d = Descriptor.getInstance();
        Map<Integer, Subject> ttSubjects = d.getTimeTable().getSubjects();

        if ((subject = ttSubjects.get(id)) == null) {
            //throw exception - delete later
            System.out.println("subject ID: " + id + " does not exist in tt");
        }
    }


    @Override
    public String toString() {
        return "Requirement{" +
                "hours=" + hours +
                ", subject=" + subject +
                '}';
    }
}
