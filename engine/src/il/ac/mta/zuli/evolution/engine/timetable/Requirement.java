package il.ac.mta.zuli.evolution.engine.timetable;

import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ETTStudy;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Requirement {
    //required hours per subject
    private int hours;
    private Subject subject;

    public Requirement(@NotNull ETTStudy ettRequirement, Map<Integer, Subject> existingSubjects) {
        setHours(ettRequirement.getHours());
        setSubject(ettRequirement.getSubjectId(), existingSubjects);
    }

    public int getHours() {
        return hours;
    }

    private void setHours(int hours) {
        if (hours > 0) {
            this.hours = hours;
        } else {
            //TODO throw exception
            System.out.println("hours are 0 or negative");
        }
    }

    public Subject getSubject() {
        return subject;
    }

    private void setSubject(int id, Map<Integer, Subject> existingSubjects) {

        if ((subject = existingSubjects.get(id)) == null) {
            //TODO throw exception
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
