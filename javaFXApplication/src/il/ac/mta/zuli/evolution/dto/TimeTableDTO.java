package il.ac.mta.zuli.evolution.dto;

import java.util.Map;
import java.util.Set;

public class TimeTableDTO {
    private final int days;
    private final int hours;
    private final Map<Integer, SubjectDTO> subjects;
    private final Map<Integer, TeacherDTO> teachers;
    private final Map<Integer, SchoolClassDTO> schoolClasses;
    private final Set<RuleDTO> rules;

    public TimeTableDTO(int days, int hours, Map<Integer, SubjectDTO> subjects,
                        Map<Integer, TeacherDTO> teachers,
                        Map<Integer, SchoolClassDTO> schoolClasses,
                        Set<RuleDTO> rules) {
        this.days = days;
        this.hours = hours;
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


