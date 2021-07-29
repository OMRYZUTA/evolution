package il.ac.mta.zuli.evolution.dto;

import java.util.Map;

public class TeacherDTO {
    private final int id;
    private final String name;
    private final Map<Integer, SubjectDTO> subjects;

    public TeacherDTO(int id, String name, Map<Integer, SubjectDTO> subjects) {
        this.id = id;
        this.name = name;
        this.subjects = subjects;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<Integer, SubjectDTO> getSubjects() {
        return subjects;
    }
}
