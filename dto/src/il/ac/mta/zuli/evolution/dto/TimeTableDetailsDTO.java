package il.ac.mta.zuli.evolution.dto;

import java.util.Collection;

public class TimeTableDetailsDTO {
    Collection<SubjectDTO> subjects;
    Collection<TeacherDTO> teachers;
    Collection<SchoolClassDTO> schoolClasses;

    public TimeTableDetailsDTO(Collection<SubjectDTO> subjects) {
        this.subjects = subjects;
    }

    public Collection<SubjectDTO> getSubjects() {
        return (subjects);
    }
}


