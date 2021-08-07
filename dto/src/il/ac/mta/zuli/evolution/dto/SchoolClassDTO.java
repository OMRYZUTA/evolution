package il.ac.mta.zuli.evolution.dto;

import java.util.List;
import java.util.Objects;

public class SchoolClassDTO {
    private final int id;
    private final String name;
    private final List<RequirementDTO> requirements;

    public SchoolClassDTO(int id, String name, List<RequirementDTO> requirements) {
        this.id = id;
        this.name = name;
        this.requirements = requirements;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<RequirementDTO> getRequirements() {
        return requirements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchoolClassDTO that = (SchoolClassDTO) o;
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
