package il.ac.mta.zuli.evolution.engine.timetable;

import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex3.ETTSubject;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Subject implements Comparable {
    private int id;
    private String name;

    public Subject(@NotNull ETTSubject s) {
        setId(s.getId());
        setName(s.getName());
    }

    public String getName() {
        return name;
    }

    private void setName(@NotNull String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return id == subject.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return this.id - ((Subject) o).getId();
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id+
                ", name='" + name + '\'' +
                '}';
    }
}
