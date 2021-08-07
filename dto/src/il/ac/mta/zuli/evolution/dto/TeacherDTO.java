package il.ac.mta.zuli.evolution.dto;

import java.util.Map;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeacherDTO that = (TeacherDTO) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "(id " + id +
                ") " + name;
    }
}
