package il.ac.mta.zuli.evolution.engine.data;

import il.ac.mta.zuli.evolution.engine.data.generated.ETTSubject;

public class Subject {
    private int id;
    private String name;

    public Subject(ETTSubject s) {
        setId(s.getId());
        setName(s.getName());
    }

    public String getName() {
        return name;
    }

    //TODO return to private
    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
