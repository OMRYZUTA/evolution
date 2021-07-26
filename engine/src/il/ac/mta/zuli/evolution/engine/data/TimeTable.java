package il.ac.mta.zuli.evolution.engine.data;

import il.ac.mta.zuli.evolution.engine.data.generated.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;


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

    private void setTeachers(@NotNull ETTTeachers ettTeachers) {
        this.teachers = new HashMap<Integer, Teacher>();
        List<ETTTeacher> teacherList = ettTeachers.getETTTeacher();

        for (ETTTeacher t : teacherList) {
            Teacher newTeacher = new Teacher(t);
            //putIfAbsent - If the specified key is not already associated with a value (or is mapped to null) associates it with the given value and returns null, else returns the current value.
            this.teachers.putIfAbsent(t.getId(), newTeacher);

            //add subjects to the teacher we just created
            List<ETTTeaches> teaches = t.getETTTeaching().getETTTeaches();

            for (ETTTeaches s : teaches) {
                Subject temp = this.subjects.get(s.getSubjectId()); //only adding subject to teacher if the subject exists in the timeTable
                newTeacher.addSubject(temp);
            }
        }
    }

    public Map<Integer, Subject> getSubjects() {
        return Collections.unmodifiableMap(subjects);
    }

    private void setSubjects(@NotNull ETTSubjects ettSubjects) {
        this.subjects = new HashMap<Integer, Subject>();
        List<ETTSubject> subjectList = ettSubjects.getETTSubject();

        for (ETTSubject s : subjectList) {
            this.subjects.putIfAbsent(s.getId(), new Subject(s));
        }
    }

    public Map<Integer, SchoolClass> getSchoolClasses() {
        return Collections.unmodifiableMap(schoolClasses);
    }

    private void setSchoolClasses(@NotNull ETTClasses ettClasses) {
        this.schoolClasses = new HashMap<Integer, SchoolClass>();
        List<ETTClass> classList = ettClasses.getETTClass();

        for (ETTClass c : classList) {
            this.schoolClasses.putIfAbsent(c.getId(), new SchoolClass(c));
        }
    }

    public List<Rule> getRules() {
        return Collections.unmodifiableList(rules);
    }

    //complete - delete later
    private void setRules(ETTRules rules) {
        this.rules = new ArrayList<Rule>();
    }

    @Override
    public String toString() {
        return "TimeTable{" +
                "days=" + days +
                ", hours=" + hours + System.lineSeparator() +
                ", teachers=" + teachers + System.lineSeparator() +
                ", subjects=" + subjects + System.lineSeparator() +
                ", schoolClasses=" + schoolClasses + System.lineSeparator() +
                ", rules=" + rules +
                '}';
    }
}
