package il.ac.mta.zuli.evolution.engine.timetable;

import il.ac.mta.zuli.evolution.engine.rules.*;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class TimeTable {
    private int days;
    private int hours;
    private Map<Integer, Teacher> teachers;
    private Map<Integer, Subject> subjects;
    private Map<Integer, SchoolClass> schoolClasses;
    private Set<Rule> rules; //a rule doesn't repeat
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
                throw new RuntimeException("UI report error: teacher ID " + t.getId() + " not according to required count");//throw exception - need to think about it
            }
            this.teachers.put(t.getId(), new Teacher(t, this.subjects));
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
                throw new RuntimeException("UI report error: subject ID " + s.getId() + " not according to required count");//throw exception - need to think about it
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
                throw new RuntimeException("UI report error: schoolClass ID " + c.getId() + " not according to required count");//throw exception - need to think about it
            }
            this.schoolClasses.put(c.getId(), new SchoolClass(c, this.subjects, (this.hours * this.days)));
        }
    }

    public Set<Rule> getRules() {
        return Collections.unmodifiableSet(rules);
    }

    private void setRules(@NotNull ETTRules ettRules) {
        this.rules = new HashSet<>();
        List<ETTRule> ruleList = ettRules.getETTRule();
        Rule ruleToAdd;

        //TODO toLower()?
        for (ETTRule r : ruleList) {
            switch (r.getETTRuleId()) {
                case "TeacherIsHuman":
                    ruleToAdd = new TeacherIsHuman(r.getType());
                    break;
                case "Singularity":
                    ruleToAdd = new Singularity(r.getType());
                    break;
                case "Knowledgeable":
                    ruleToAdd = new Knowledgeable(r.getType());
                    break;
                case "Satisfactory":
                    ruleToAdd = new Satisfactory(r.getType(), this.schoolClasses);
                    break;
                default:
                    //TODO throw exception?
                    throw new IllegalStateException("Unexpected value: " + r.getETTRuleId());
            }

            //TODO check for duplicate rules
            this.rules.add(ruleToAdd);
        }
    }

    public int getHardRulesWeight() {
        return hardRulesWeight;
    }

    private void setHardRulesWeight(int hardRulesWeight) {
        this.hardRulesWeight = hardRulesWeight;
    }

    public List<Integer> getTeachersThatTeachSubject(int subjectID) {
        List<Integer> subjectTeacherList = new ArrayList<>();

        for (Teacher teacher : teachers.values()) {
            if ((teacher.getSubjects()).containsKey(subjectID)) {
                subjectTeacherList.add(teacher.getId());
            }
        }

        return subjectTeacherList;
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
