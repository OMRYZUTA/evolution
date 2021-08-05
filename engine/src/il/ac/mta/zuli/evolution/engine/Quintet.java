package il.ac.mta.zuli.evolution.engine;


import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;
import il.ac.mta.zuli.evolution.engine.timetable.Subject;
import il.ac.mta.zuli.evolution.engine.timetable.Teacher;

import java.time.DayOfWeek;
import java.util.Objects;

public class Quintet {
    private DayOfWeek day;
    private int hour;
    private Teacher teacher;
    private SchoolClass schoolClass;
    private Subject subject;

    public Quintet(DayOfWeek day, int hour, Teacher teacher, SchoolClass schoolClass, Subject subject) {
        this.day = day;
        this.hour = hour;
        this.teacher = teacher;
        this.schoolClass = schoolClass;
        this.subject = subject;
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

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public int getSchoolClassID() {
        return schoolClass.getId();
    }

    public Subject getSubject() {
        return subject;
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

    @Override
    public String toString() {
        if (teacher != null && schoolClass != null && subject != null) {
            return "{day=" + day +
                    ", hour=" + hour +
                    ", teacher=" + teacher.getId() +
                    ", schoolClass=" + schoolClass.getId() +
                    ", subject=" + subject.getId() +
                    '}';
        } else {
            return "invalid quintet: teacheer/class/subject are null";
        }
    }
}
