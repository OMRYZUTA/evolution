package il.ac.mta.zuli.evolution.dto;

import java.util.Objects;

public class RuleDTO {
    private final String name;
    private final String type;
    private final String params;

    public RuleDTO(String name, String type, String params) {
        this.name = name;
        this.type = type;
        this.params = params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RuleDTO ruleDTO = (RuleDTO) o;
        return name.equals(ruleDTO.name) && type.equals(ruleDTO.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
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

    public String getParams() {
        return params;
    }

    public boolean isHardRule() {
        return this.type.compareToIgnoreCase("hard") == 0;
    }
}
