package il.ac.mta.zuli.evolution.engine.data;

import java.util.Map;

public class SchoolClass {
    private int id;
    private String name;
    private Map<Integer,Integer> hoursPerSubjects;

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

    public Map<Integer, Integer> getHoursPerSubjects() {
        return hoursPerSubjects;
    }

    public void setHoursPerSubjects(Map<Integer, Integer> hoursPerSubjects) {
        this.hoursPerSubjects = hoursPerSubjects;
    }
}
