package il.ac.mta.zuli.evolution.dto;

import java.util.List;

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
    public String toString() {
        return "SchoolClassDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", requirements=" + requirements +
                '}';
    }
}
