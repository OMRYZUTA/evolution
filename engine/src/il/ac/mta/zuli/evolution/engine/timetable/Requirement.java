package il.ac.mta.zuli.evolution.engine.timetable;

import il.ac.mta.zuli.evolution.engine.exceptions.EmptyCollectionException;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex1.ETTStudy;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public class Requirement {
    //required hours per subject
    private int hours;
    private Subject subject;

    public Requirement(@NotNull ETTStudy ettRequirement,@NotNull Map<Integer, Subject> existingSubjects) {
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
            throw new ValidationException("invalid hours input - hours are 0 or negative");
        }
    }

    public Subject getSubject() {
        return subject;
    }

    private void setSubject(int id, Map<Integer, Subject> existingSubjects) {
        if(existingSubjects.size()==0){
            throw new EmptyCollectionException("requirement got empty collection");
        }
        subject = existingSubjects.get(id);
        if ( subject== null) {
            throw new ValidationException("subject ID: " + id + " does not exist in time table");
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
