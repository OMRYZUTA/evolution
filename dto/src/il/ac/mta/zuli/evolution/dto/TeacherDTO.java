package il.ac.mta.zuli.evolution.dto;

import java.util.Map;

public class TeacherDTO {
    private final int id;
    private final String name;
    private final Map<Integer, SubjectDTO> subjects;

    //TODO suggestion from Noam - in DTOs hold only lists not maps and sets?? think about it
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

    @Override
    public String toString() {
        return "(id " + id +
                ") " + name;
    }
}
