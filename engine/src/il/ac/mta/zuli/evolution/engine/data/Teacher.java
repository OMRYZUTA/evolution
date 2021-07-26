package il.ac.mta.zuli.evolution.engine.data;

import il.ac.mta.zuli.evolution.engine.data.generated.ETTTeacher;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Teacher {
    private int id;
    private String name;
    private final Map<Integer, Subject> subjects; //the subjects the teacher teaches

    public Teacher(ETTTeacher t) {
        setName(t.getETTName());
        setId(t.getId());
        this.subjects = new HashMap<Integer, Subject>(); //adding subjects on the outsife, after the ctor
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public void addSubject(Subject subjectToAdd) {
        //check id is unique before adding
        this.subjects.put(subjectToAdd.getId(), subjectToAdd);
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
