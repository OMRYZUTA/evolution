package il.ac.mta.zuli.evolution.engine;


import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;
import il.ac.mta.zuli.evolution.engine.timetable.Subject;
import il.ac.mta.zuli.evolution.engine.timetable.Teacher;

import java.time.DayOfWeek;
import java.util.Comparator;
import java.util.Objects;

public class Quintet {
    private DayOfWeek day;
    private int hour;
    private Teacher teacher;
    private SchoolClass schoolClass;
    private Subject subject;
    private final static Comparator<Quintet> compareByDay = Comparator.comparing(Quintet::getDay);
    private final static Comparator<Quintet> compareByHour = Comparator.comparing(Quintet::getHour);
    private final static Comparator<Quintet> compareByTeacher = Comparator.comparing(Quintet::getTeacherID);
    private final static Comparator<Quintet> compareBySchoolClass = Comparator.comparing(Quintet::getSchoolClassID);
    private final static Comparator<Quintet> compareBySubject = Comparator.comparing(Quintet::getSubjectID);

    public Quintet(DayOfWeek day, int hour, Teacher teacher, SchoolClass schoolClass, Subject subject) {
        this.day = day;
        this.hour = hour;
        this.teacher = teacher;
        this.schoolClass = schoolClass;
        this.subject = subject;
    }

    public static Comparator<Quintet> getRawComparator() {
        Comparator<Quintet> rawComparator = compareByDay
                .thenComparing(compareByHour)
                .thenComparing(compareBySchoolClass)
                .thenComparing(compareByTeacher)
                .thenComparing(compareBySubject);

        return rawComparator;
    }

    public static Comparator<Quintet> getTeacherComparator() {
        Comparator<Quintet> teacherComparator = compareByTeacher
                .thenComparing(compareByDay)
                .thenComparing(compareByHour)
                .thenComparing(compareBySchoolClass)
                .thenComparing(compareBySubject);

        return teacherComparator;
    }

    public static Comparator<Quintet> getSchoolClassComparator() {
        Comparator<Quintet> schoolClassComparator = compareBySchoolClass
                .thenComparing(compareByDay)
                .thenComparing(compareByHour)
                .thenComparing(compareByTeacher)
                .thenComparing(compareBySubject);

        return schoolClassComparator;
    }

    public DayOfWeek getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public int getTeacherID() {
        return teacher.getId();
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public int getSchoolClassID() {
        return schoolClass.getId();
    }

    public Subject getSubject() {
        return subject;
    }

    public int getSubjectID() {
        return subject.getId();
    }

    private void setDay(DayOfWeek day) {
        this.day = day;
    }

    private void setHour(int hour) {
        this.hour = hour;
    }

    private void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    private void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quintet quintet = (Quintet) o;
        return hour == quintet.hour && day == quintet.day && teacher.equals(quintet.teacher) && schoolClass.equals(quintet.schoolClass) && subject.equals(quintet.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, hour, teacher, schoolClass, subject);
    }

    //    @Override
//    public String toString() {
//        if (teacher != null && schoolClass != null && subject != null) {
//            return "{day=" + day +
//                    ", hour=" + hour +
//                    ", teacher=" + teacher.getId() +
//                    ", schoolClass=" + schoolClass.getId() +
//                    ", subject=" + subject.getId() +
//                    '}';
//        } else {
//            return "invalid quintet: teacheer/class/subject are null";
//        }
//    }
    @Override
    public String toString() {
        // for debugging purposes.
        return "< " + day + ", " + hour + ", " + teacher.getId() + ", " + schoolClass.getId() + ", " + subject.getId() + ">";
    }

}
