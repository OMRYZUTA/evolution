package il.ac.mta.zuli.evolution.dto;

import java.time.DayOfWeek;
import java.util.Objects;

public class QuintetDTO {
    private final DayOfWeek day;
    private final int hour;
    private final TeacherDTO teacher;
    private final SchoolClassDTO schoolClass;
    private final SubjectDTO subject;

    public QuintetDTO(DayOfWeek day, int hour, TeacherDTO teacher, SchoolClassDTO schoolClass, SubjectDTO subject) {
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

    public TeacherDTO getTeacher() {
        return teacher;
    }

    public SchoolClassDTO getSchoolClass() {
        return schoolClass;
    }

    public SubjectDTO getSubject() {
        return subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuintetDTO that = (QuintetDTO) o;
        return hour == that.hour && day == that.day && teacher.equals(that.teacher) && schoolClass.equals(that.schoolClass) && subject.equals(that.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, hour, teacher, schoolClass, subject);
    }

    @Override
    public String toString() {
        return "QuintetDTO{" +
                "day=" + day +
                ", hour=" + hour +
                ", teacher=" + teacher +
                ", schoolClass=" + schoolClass +
                ", subject=" + subject +
                '}';
    }

    /*@Override
    public  String toString(){
        // for debugging purposes.
        return "< "+ day+", "+ hour +", "+teacher.getId()+ ", "+ schoolClass.getId()+", "+subject.getId()+ ">";
    }*/
}
