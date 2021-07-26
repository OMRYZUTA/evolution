package il.ac.mta.zuli.evolution.engine.data;

import il.ac.mta.zuli.evolution.engine.data.generated.ETTClass;
import il.ac.mta.zuli.evolution.engine.data.generated.ETTRequirements;
import il.ac.mta.zuli.evolution.engine.data.generated.ETTStudy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SchoolClass {
    private int id;
    private String name;
    private List<Requirement> requirements; //requirement is hours per subject

    public SchoolClass(ETTClass srcClass) {
        setId(srcClass.getId());
        setName(srcClass.getETTName());
        setRequirements(srcClass.getETTRequirements());
    }


    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public List<Requirement> getRequirements() {
        return Collections.unmodifiableList(requirements);
    }

    private void setRequirements(ETTRequirements ettRequirements) {
        this.requirements = new ArrayList<Requirement>();
        List<ETTStudy> requirementList = ettRequirements.getETTStudy();

        for (ETTStudy r : requirementList) {
            this.requirements.add(new Requirement(r));
        }
    }

    @Override
    public String toString() {
        return "SchoolClass{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", requirements=" + requirements +
                '}';
    }
}
