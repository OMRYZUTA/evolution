package il.ac.mta.zuli.evolution.dto;

import il.ac.mta.zuli.evolution.engine.Quintet;

import java.util.Comparator;
import java.util.Objects;

public class QuintetDTO {
    private final int day;
    private final int hour;
    private final int teacherID;
    private final String teacherName;
    private final int schoolClassID;
    private final String schoolClassName;
    private final int subjectID;
    private final String subjectName;

    private final static Comparator<QuintetDTO> compareByDay = Comparator.comparing(QuintetDTO::getDay);
    private final static Comparator<QuintetDTO> compareByHour = Comparator.comparing(QuintetDTO::getHour);
    private final static Comparator<QuintetDTO> compareByTeacher = Comparator.comparing(QuintetDTO::getTeacherID);
    private final static Comparator<QuintetDTO> compareBySchoolClass = Comparator.comparing(QuintetDTO::getSchoolClassID);
    private final static Comparator<QuintetDTO> compareBySubject = Comparator.comparing(QuintetDTO::getSubjectID);

    public QuintetDTO(Quintet quintet) {
        this.day = quintet.getDay().getValue() - 1;
        this.hour = quintet.getHour();
        this.teacherID = quintet.getTeacherID();
        this.teacherName = quintet.getTeacher().getName();
        this.schoolClassID = quintet.getSchoolClassID();
        this.schoolClassName = quintet.getSchoolClass().getName();
        this.subjectID = quintet.getSubjectID();
        this.subjectName = quintet.getSubject().getName();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuintetDTO that = (QuintetDTO) o;
        return hour == that.hour && day == that.day &&
                (teacherID == that.teacherID) &&
                (schoolClassID == that.schoolClassID) &&
                (subjectID == that.subjectID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, hour, teacherID, schoolClassID, subjectID);
    }

    @Override
    public String toString() {
        return "QuintetDTO{" +
                "day=" + day +
                ", hour=" + hour +
                ", teacher=" + teacherID +
                ", schoolClass=" + schoolClassID +
                ", subject=" + subjectID +
                '}';
    }

    public static Comparator<QuintetDTO> getRawComparator() {
        //sort by day,hour,class,teacher,subject
        Comparator<QuintetDTO> dhComparator = compareByDay
                .thenComparing(compareByHour)
                .thenComparing(compareBySchoolClass)
                .thenComparing(compareByTeacher)
                .thenComparing(compareBySubject);

        return dhComparator;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public int getSchoolClassID() {
        return schoolClassID;
    }

    public int getSubjectID() {
        return subjectID;
    }
}
