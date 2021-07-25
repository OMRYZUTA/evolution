package il.ac.mta.zuli.evolution.engine.data;

import il.ac.mta.zuli.evolution.engine.data.generated.ETTTeacher;
import il.ac.mta.zuli.evolution.engine.data.generated.ETTTeaches;
import il.ac.mta.zuli.evolution.engine.data.generated.ETTTeaching;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Teacher {
    private int id;
    private String name;
    private Map<Integer, Subject> subjects;

    public Teacher(ETTTeacher t) {
        setName(t.getETTName());
        setId(t.getId());
        setSubjects(t.getETTTeaching());
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

    private void setSubjects(ETTTeaching teachings) {
        this.subjects = new HashMap<>();
//        List<ETTTeaches> temp = new ArrayList<ETTTeaches>();
        List<ETTTeaches> temp = teachings.getETTTeaches();
        for (ETTTeaches teaches : temp
        ) {
            int subjectIt = teaches.getSubjectId();

        }
    }
}

//    public List<Integer> getSubjects() {
//        return subjects;
//    }
//
//    public void setSubjects(List<Integer> subjects) {

//    public void setSubjects(List<Integer> subjects) {
//
//        subjects = new ArrayList<Integer>();
//        for (int subject : subjects) {
//            subjects.add(subject);
//        }
//    }
//
//    @Override
//    public String toString() {
//        return String.format(id + " " + name + subjects);
//    }
//}
