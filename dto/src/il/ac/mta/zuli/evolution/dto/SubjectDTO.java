package il.ac.mta.zuli.evolution.dto;

public class SubjectDTO {
    private final int id;
    private final String name;

    public SubjectDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "(id " + id +
                ") " + name;
    }
}
