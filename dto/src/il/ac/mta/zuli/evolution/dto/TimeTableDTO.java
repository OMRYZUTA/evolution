package il.ac.mta.zuli.evolution.dto;

import java.util.Map;

public class TimeTableDTO {
    Map<Integer, SubjectDTO> subjects;
    Map<Integer, TeacherDTO> teachers;
    Map<Integer, SchoolClassDTO> schoolClasses;

    public TimeTableDTO(Map<Integer, SubjectDTO> subjects, Map<Integer, TeacherDTO> teachers,
                        Map<Integer, SchoolClassDTO> schoolClasses) {
        this.subjects = subjects;
        this.teachers = teachers;
        this.schoolClasses = schoolClasses;
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
}


