package il.ac.mta.zuli.evolution.engine.timetable;

import il.ac.mta.zuli.evolution.engine.exceptions.EmptyCollectionException;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.rules.Rule;
import il.ac.mta.zuli.evolution.engine.rules.RuleFactory;
import il.ac.mta.zuli.evolution.engine.rules.RuleType;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex3.*;
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
    //Ex3 additions:
    private int ID; // ID is index in dataManager TT-list
    private String uploadedBy;

    public TimeTable(@NotNull ETTTimeTable tt) {
        setHardRulesWeight(tt.getETTRules().getHardRulesWeight());
        setDays(tt.getDays());
        setHours(tt.getHours());
        setSubjects(tt.getETTSubjects());
        setSchoolClasses(tt.getETTClasses());
        setTeachers(tt.getETTTeachers());
        setRules(tt.getETTRules());
    }

    //#region getters
    public int getID() {

        return ID;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public int getDays() {
        return days;
    }

    public int getHours() {
        return hours;
    }

    public Map<Integer, Teacher> getTeachers() {
        return Collections.unmodifiableMap(teachers);
    }

    public Map<Integer, Subject> getSubjects() {
        return Collections.unmodifiableMap(subjects);
    }

    public Map<Integer, SchoolClass> getSchoolClasses() {
        return Collections.unmodifiableMap(schoolClasses);
    }

    public Set<Rule> getRules() {
        return Collections.unmodifiableSet(rules);
    }

    public int getHardRulesWeight() {
        return hardRulesWeight;
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

    public int getNumOfTeachers() {
        return teachers.size();
    }

    public int getNumOfSubjects() {
        return subjects.size();
    }

    public int getNumOfClasses() {
        return schoolClasses.size();
    }

    public int getNumOfSoftRules() {
        return (int) rules.stream()
                .filter(rule -> rule.getRuleType() == RuleType.SOFT)
                .count();
    }

    public int getNumOfHardRules() {
        return (int) rules.stream()
                .filter(rule -> rule.getRuleType() == RuleType.HARD)
                .count();
    }
    //#endregion

    //#region setters
    private void setDays(int days) {
        if (days > 0) {
            this.days = days;
        } else {
            throw new ValidationException("The number of TimeTable days must be positive");
        }
    }

    private void setHours(int hours) {
        if (hours > 0) {
            this.hours = hours;
        } else {
            throw new ValidationException("The number of TimeTable hours must be positive");
        }
    }

    private void setTeachers(@NotNull ETTTeachers ettTeachers) {
        if (ettTeachers.getETTTeacher().size() == 0) {
            throw new EmptyCollectionException("There are no teachers in the file, and there must be at least 1.");
        }

        this.teachers = new HashMap<>();
        List<ETTTeacher> teacherList = ettTeachers.getETTTeacher();
        teacherList.sort(Comparator.comparingInt(ETTTeacher::getId)); //sorting in order to check the IDs of subjects in file cover numbers 1-numOfSubjects
        ETTTeacher t;

        for (int i = 0; i < teacherList.size(); i++) {
            t = teacherList.get(i);

            if (i + 1 != t.getId()) {
                throw new ValidationException("Teacher ID not according to required count: "
                        + t.getId() + ", " + t.getETTName());
            }

            try {
                Teacher teacher = new Teacher(t, this.subjects, days, hours);
                this.teachers.put(t.getId(), teacher);
            } catch (ValidationException e) {
                throw new ValidationException("Failed creating teacher " + t.getId() + ", " + t.getETTName(), e);
            }
        }
    }

    private void setSubjects(@NotNull ETTSubjects ettSubjects) {
        if (ettSubjects.getETTSubject().size() == 0) {
            throw new EmptyCollectionException("\"There are no subjects in the file, and there must be at least 1. ");
        }

        this.subjects = new HashMap<>();
        List<ETTSubject> subjectList = ettSubjects.getETTSubject();
        subjectList.sort(Comparator.comparingInt(ETTSubject::getId)); //sorting in order to check the IDs of subjects in file cover numbers 1-numOfSubjects
        ETTSubject s;

        for (int i = 0; i < subjectList.size(); i++) {
            s = subjectList.get(i);

            if (i + 1 != s.getId()) {
                throw new ValidationException("Subject ID not according to required count" + s.getId() + s.getName());
            }

            try {
                Subject subject = new Subject(s);
                this.subjects.put(s.getId(), subject);
            } catch (ValidationException e) {
                throw new ValidationException("Failed creating subject " + s.getId() + s.getName(), e);
            }
        }
    }

    private void setSchoolClasses(@NotNull ETTClasses ettClasses) {
        if (ettClasses.getETTClass().size() == 0) {
            throw new EmptyCollectionException("There are no classes in the file, and there must be at least 1. ");
        }

        this.schoolClasses = new HashMap<>();
        List<ETTClass> classList = ettClasses.getETTClass();
        //sorting in order to check the IDs of classes in file cover numbers 1-numOfClasses
        classList.sort(Comparator.comparingInt(ETTClass::getId));
        ETTClass c;

        for (int i = 0; i < classList.size(); i++) {
            c = classList.get(i);

            if (i + 1 != c.getId()) {
                throw new ValidationException("Class ID not according to required count " + c.getId() + c.getETTName());
            }

            try {
                SchoolClass schoolClass = new SchoolClass(c, this.subjects, (this.hours * this.days));
                this.schoolClasses.put(c.getId(), schoolClass);
            } catch (ValidationException e) {
                throw new ValidationException("Failed creating school class " + c.getId() + " " + c.getETTName(), e);
            }
        }
    }

    private void setRules(@NotNull ETTRules ettRules) {
        this.rules = new HashSet<>();
        List<ETTRule> ruleList = ettRules.getETTRule();

        Rule ruleToAdd;

        for (ETTRule rule : ruleList) {
            ruleToAdd = RuleFactory.createRule(rule, days, new ArrayList<>(this.teachers.values()), new ArrayList<>(this.schoolClasses.values()));
            //add returns false if element already exists in set
            if (!this.rules.add(ruleToAdd)) {
                throw new ValidationException("Failed to add rule " + rule.getETTRuleId() + ". Duplicate rules are not permitted");
            }
        }
    }

    private void setHardRulesWeight(int hardRulesWeight) {
        if (0 <= hardRulesWeight && hardRulesWeight <= 100) {
            this.hardRulesWeight = hardRulesWeight;
        } else {
            throw new ValidationException("Invalid Hard-Rules Weight: " + hardRulesWeight + ". Weight must be integer in the range 0-100");
        }
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
//#endregion


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeTable timeTable = (TimeTable) o;
        return ID == timeTable.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
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
