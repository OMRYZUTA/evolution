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

    private void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }
}
