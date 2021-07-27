package il.ac.mta.zuli.evolution.engine.data;

import il.ac.mta.zuli.evolution.engine.data.generated.ETTTeacher;
import il.ac.mta.zuli.evolution.engine.data.generated.ETTTeaches;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Teacher {
    private int id;
    private String name;
    private Map<Integer, Subject> subjects; //the subjects the teacher teaches

    public Teacher(ETTTeacher t) {
        setName(t.getETTName());
        setId(t.getId());
        setSubjects(t.getETTTeaching().getETTTeaches());
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

    private void setSubjects(List<ETTTeaches> subjectList) {
        Map<Integer, Subject> existingSubjects = Descriptor.getInstance().getTimeTable().getSubjects();
        Subject subjectToAdd = null;

        for (ETTTeaches s : subjectList) {
            //only adding subject to teacher if the subject exists in  timeTable
            if ((subjectToAdd = existingSubjects.get(s.getSubjectId())) != null) {
                this.subjects.put(subjectToAdd.getId(), subjectToAdd);
            } else {
                //throw exception - delete later
                System.out.println("Teacher " + id + "has subject that doesn't exist in the timetable");
            }
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
