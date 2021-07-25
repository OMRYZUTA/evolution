package il.ac.mta.zuli.evolution.engine.data;

import il.ac.mta.zuli.evolution.engine.data.generated.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TimeTable {
    private int days;
    private int hours;
    private Map<Integer, Teacher> teachers;
    private Map<Integer, Subject> subjects;
    private Map<Integer, SchoolClass> schoolClasses;
    private List<Rule> rules;//still need to figure out rules - delete later


    public TimeTable(ETTTimeTable tt) {
        setRules(tt.getETTRules());
        setDays(tt.getDays());
        setHours(tt.getHours());
        setSubjects(tt.getETTSubjects());
        setSchoolClasses(tt.getETTClasses());
        setTeachers(tt.getETTTeachers());
    }

    public int getDays() {
        return days;
    }

    private void setDays(int days) {
        this.days = days;
    }

    public int getHours() {
        return hours;
    }

    private void setHours(int hours) {
        this.hours = hours;
    }

    public Map<Integer, Teacher> getTeachers() {
        return Collections.unmodifiableMap(teachers);
    }

    private void setTeachers(ETTTeachers teachers) {
        this.teachers = new HashMap<Integer, Teacher>();

        //check parameter is not null - delete later
        for (ETTTeacher t : teachers.getETTTeacher()
        ) {
            //check ID is unique first - delete later
            this.teachers.put(t.getId(), new Teacher(t));
        }
    }

    public Map<Integer, Subject> getSubjects() {
        return Collections.unmodifiableMap(subjects);
    }

    private void setSubjects(ETTSubjects subjects) {
        this.subjects = new HashMap<Integer, Subject>();

        for (ETTSubject s : subjects.getETTSubject()
        ) {
            //check ID is unique first - delete later
            this.subjects.put(s.getId(), new Subject(s));
        }
    }

    public Map<Integer, SchoolClass> getSchoolClasses() {
        return Collections.unmodifiableMap(schoolClasses);
    }

    private void setSchoolClasses(ETTClasses classes) {
        this.schoolClasses = new HashMap<Integer, SchoolClass>();

        for (ETTClass c : classes.getETTClass()
        ) {
            this.schoolClasses.put(c.getId(), new SchoolClass(c));
        }

    }

    public List<Rule> getRules() {
        return Collections.unmodifiableList(rules);
    }

    //complete - delete later
    private void setRules(ETTRules rules) {
    }

    @Override
    public String toString() {
        return "TimeTable{" +
                "days=" + days +
                ", hours=" + hours +
                '}';
    }
}
