package il.ac.mta.zuli.evolution.dto;

public class RuleDTO {
    String name;
    String type;

    public RuleDTO(String name, String type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return name + ", type: " + type.toLowerCase();
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
