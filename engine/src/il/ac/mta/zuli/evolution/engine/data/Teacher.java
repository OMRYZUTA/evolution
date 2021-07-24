package il.ac.mta.zuli.evolution.engine.data;

import il.ac.mta.zuli.evolution.engine.data.generated.ETTTeacher;

import java.util.Map;

public class Teacher {
    private int id;
    private String name;
    private Map<Integer, Subject> subjects;

    public Teacher(ETTTeacher loadedData) {
        setName(loadedData.getETTName());
        setId(loadedData.getId());

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
