package il.ac.mta.zuli.evolution.dto;

import java.util.Map;
import java.util.Set;

public class TimeTableDTO {
    private int days;
    private int hours;
    Map<Integer, SubjectDTO> subjects;
    Map<Integer, TeacherDTO> teachers;
    Map<Integer, SchoolClassDTO> schoolClasses;
    Set<RuleDTO> rules;

    public TimeTableDTO(Map<Integer, SubjectDTO> subjects,
                        Map<Integer, TeacherDTO> teachers,
                        Map<Integer, SchoolClassDTO> schoolClasses,
                        Set<RuleDTO> rules) {
        this.subjects = subjects;
        this.teachers = teachers;
        this.schoolClasses = schoolClasses;
        this.rules = rules;
    }

    public Map<Integer, SubjectDTO> getSubjects() {
        return (subjects);
    }

    public Map<Integer, TeacherDTO> getTeachers() {
        return teachers;
    }

    public Map<Integer, SchoolClassDTO> getSchoolClasses() {
        return schoolClasses;
    }

    public Set<RuleDTO> getRules() {
        return rules;
    }

    public int getDays() {
        return days;
    }

    public int getHours() {
        return hours;
    }
}


