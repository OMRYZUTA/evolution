package il.ac.mta.zuli.evolution.engine.timetable;

import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ETTTeacher;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ETTTeaches;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Teacher {
    private int id;
    private String name;
    private Map<Integer, Subject> subjects; //the subjects the teacher teaches

    public Teacher(@NotNull ETTTeacher t, Map<Integer, Subject> existingSubjects) {
        setName(t.getETTName());
        setId(t.getId());
        setSubjects(t.getETTTeaching().getETTTeaches(), existingSubjects);
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
        this.subjects = new HashMap<>();
        Subject subjectToAdd;

        for (ETTTeaches s : subjectList) {
            if ((subjectToAdd = existingSubjects.get(s.getSubjectId())) == null) {
                //TODO throw exception
                System.out.println("Teacher " + id + "has subject that doesn't exist in the timetable");
            }
            this.subjects.put(subjectToAdd.getId(), subjectToAdd);
        }
    }

    public Map<Integer, Subject> getSubjects() {
        return Collections.unmodifiableMap(subjects);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", subjects=" + subjects +
                '}';
    }
}
