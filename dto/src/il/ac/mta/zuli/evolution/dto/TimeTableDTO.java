package il.ac.mta.zuli.evolution.dto;

import java.util.Collection;
import java.util.Map;

public class TimeTableDTO {
    Map<Integer, SubjectDTO> subjects;
    Map<Integer, SubjectDTO> teachers;
    Collection<SchoolClassDTO> schoolClasses;

    public TimeTableDTO(Map<Integer, SubjectDTO> subjects) {
        this.subjects = subjects;
    }

    public Map<Integer, SubjectDTO> getSubjects() {
        return (subjects);
    }
}


