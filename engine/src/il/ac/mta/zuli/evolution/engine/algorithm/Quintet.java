package il.ac.mta.zuli.evolution.engine.algorithm;


import il.ac.mta.zuli.evolution.engine.data.SchoolClass;
import il.ac.mta.zuli.evolution.engine.data.Subject;
import il.ac.mta.zuli.evolution.engine.data.Teacher;

import java.time.DayOfWeek;

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

    public void setDay(DayOfWeek day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public SchoolClass getSchoolClass() {
        return schoolClass;
    }

    public void setSchoolClass(SchoolClass schoolClass) {
        this.schoolClass = schoolClass;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }
}
