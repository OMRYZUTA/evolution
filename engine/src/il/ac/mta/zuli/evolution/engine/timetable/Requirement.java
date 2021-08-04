package il.ac.mta.zuli.evolution.engine.timetable;

import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ETTStudy;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

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
            throw new RuntimeException("invalid hours input - hours are 0 or negative");
        }
    }

    public Subject getSubject() {
        return subject;
    }

    private void setSubject(int id, Map<Integer, Subject> existingSubjects) {

        if ((subject = existingSubjects.get(id)) == null) {
            throw new RuntimeException("subject ID: " + id + " does not exist in tt");
        }
    }

    @Override
    public String toString() {
        return "Requirement{" +
                "hours=" + hours +
                ", subject=" + subject +
                '}';
    }

    public boolean isRequirementMet(Integer hours) {
        return this.hours == hours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Requirement that = (Requirement) o;
        return hours == that.hours && subject.equals(that.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hours, subject);
    }
}
