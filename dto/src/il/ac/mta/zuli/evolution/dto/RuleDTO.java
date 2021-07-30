package il.ac.mta.zuli.evolution.dto;

public class RuleDTO {
    String name;
    String type;
    public RuleDTO(String name,String type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String toString() {
        return
                "Rule name: " + name  +
                ", Rule type:" + type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
