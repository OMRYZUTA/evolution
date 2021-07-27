package il.ac.mta.zuli.evolution.engine.data;

import il.ac.mta.zuli.evolution.engine.data.generated.*;
import il.ac.mta.zuli.evolution.engine.rules.Rule;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class TimeTable {
    private int days;
    private int hours;
    private Map<Integer, Teacher> teachers;
    private Map<Integer, Subject> subjects;
    private Map<Integer, SchoolClass> schoolClasses;
    private List<Rule> rules;//still need to figure out rules - delete later
    private int hardRulesWeight;


    public TimeTable(@NotNull ETTTimeTable tt) {
        //TODO throw exception
        setRules(tt.getETTRules());
        setHardRulesWeight(tt.getETTRules().getHardRulesWeight());
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
        if (days > 0) {
            this.days = days;
        } else {//TODO throw exception
        }
    }

    public int getHours() {
        return hours;
    }

    private void setHours(int hours) {
        if (hours > 0) {
            this.hours = hours;
        } else {//TODO throw exception
        }
    }

    public Map<Integer, Teacher> getTeachers() {
        return Collections.unmodifiableMap(teachers);
    }

    private void setTeachers(@NotNull ETTTeachers ettTeachers) {
        this.teachers = new HashMap<>();

        List<ETTTeacher> teacherList = ettTeachers.getETTTeacher();
        //sorting in order to check the IDs of subjects in file cover numbers 1-numOfSubjects
        teacherList.sort(Comparator.comparingInt(ETTTeacher::getId));
        ETTTeacher t;

        for (int i = 0; i < teacherList.size(); i++) {
            t = teacherList.get(i);

            if (i + 1 != t.getId()) {
                //TODO throw exception
                System.out.println("UI report error: teacher ID " + t.getId() + " not according to required count");//throw exception - need to think about it
                return;
            }
            this.teachers.put(t.getId(), new Teacher(t));
        }
    }

    public Map<Integer, Subject> getSubjects() {
        return Collections.unmodifiableMap(subjects);
    }

    private void setSubjects(@NotNull ETTSubjects ettSubjects) {
        this.subjects = new HashMap<>();

        List<ETTSubject> subjectList = ettSubjects.getETTSubject();
        //sorting in order to check the IDs of subjects in file cover numbers 1-numOfSubjects
        subjectList.sort(Comparator.comparingInt(ETTSubject::getId));
        ETTSubject s;

        for (int i = 0; i < subjectList.size(); i++) {
            s = subjectList.get(i);

            if (i + 1 != s.getId()) {
                //TODO throw exception
                System.out.println("UI report error: subject ID " + s.getId() + " not according to required count");//throw exception - need to think about it
                return;
            }
            this.subjects.put(s.getId(), new Subject(s)); //no need for putIfAbsent because we're checking IDs here
        }
    }


    public Map<Integer, SchoolClass> getSchoolClasses() {
        return Collections.unmodifiableMap(schoolClasses);
    }

    private void setSchoolClasses(@NotNull ETTClasses ettClasses) {
        this.schoolClasses = new HashMap<>();

        List<ETTClass> classList = ettClasses.getETTClass();
        //sorting in order to check the IDs of classes in file cover numbers 1-numOfClasses
        classList.sort(Comparator.comparingInt(ETTClass::getId));
        ETTClass c = null;

        for (int i = 0; i < classList.size(); i++) {
            c = classList.get(i);

            if (i + 1 != c.getId()) {
                //TODO throw exception
                System.out.println("UI report error: schoolClass ID " + c.getId() + " not according to required count");//throw exception - need to think about it
                return;
            }
            this.schoolClasses.put(c.getId(), new SchoolClass(c));
        }
    }

    public List<Rule> getRules() {
        return Collections.unmodifiableList(rules);
    }

    private void setRules(@NotNull ETTRules rules) {
        //TODO throw exception
        this.rules = new ArrayList<>();
    }

    public int getHardRulesWeight() {
        return hardRulesWeight;
    }

    private void setHardRulesWeight(int hardRulesWeight) {
        this.hardRulesWeight = hardRulesWeight;
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
