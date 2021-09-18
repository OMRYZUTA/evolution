package il.ac.mta.zuli.evolution.engine.timetable;

import il.ac.mta.zuli.evolution.engine.exceptions.EmptyCollectionException;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex2.ETTTeacher;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex2.ETTTeaches;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Teacher {
    private int id;
    private String name;
    private int workingHours;
    private Map<Integer, Subject> subjects; //the subjects the teacher teaches

    public Teacher(@NotNull ETTTeacher t, @NotNull Map<Integer, Subject> existingSubjects) {
        setName(t.getETTName());
        setId(t.getId());
//        setWorkingHours(t.getETTWorkingHours()); //todo: return to this later
        setSubjects(t.getETTTeaching().getETTTeaches(), existingSubjects);
    }

    private void setWorkingHours(int ettWorkingHours) {
        if (ettWorkingHours > 0) {
            this.workingHours = ettWorkingHours;
        } else {
            throw new ValidationException("The number of working hours for "
                    + this.name +
                    " must be a positive number.");
        }
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        //no need to validate id in here because we validate it's a running count outside
        this.id = id;
    }

    public String getName() {
        return name;
    }

    private void setName(@NotNull String name) {
        this.name = name;
    }

    private void setSubjects(@NotNull List<ETTTeaches> subjectList, Map<Integer, Subject> existingSubjects) {
        if (existingSubjects.size() == 0) {
            throw new EmptyCollectionException("There are not subjects in the timetable, so the teacher has nothing to teach. ");
        }

        this.subjects = new HashMap<>();
        Subject subjectToAdd;

        if (subjectList.size() > 0) {
            for (ETTTeaches s : subjectList) {
                if ((subjectToAdd = existingSubjects.get(s.getSubjectId())) == null) {
                    throw new ValidationException("Teacher " + id + "has subject that doesn't exist in the timetable");
                }

                this.subjects.put(subjectToAdd.getId(), subjectToAdd);
            }
        }
    }

    public Map<Integer, Subject> getSubjects() {
        return Collections.unmodifiableMap(subjects);
    }

    public int getWorkingHours() {
        return workingHours;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id;
//                ", name='" + name + '\'' +
//                ", subjects=" + subjects +
//                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return id == teacher.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
