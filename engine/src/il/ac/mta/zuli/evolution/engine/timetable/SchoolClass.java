package il.ac.mta.zuli.evolution.engine.timetable;

import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ETTClass;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ETTRequirements;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ETTStudy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SchoolClass {
    private int id;
    private String name;
    private List<Requirement> requirements; //requirement is hours per subject

    public SchoolClass(ETTClass srcClass, Map<Integer, Subject> existingSubjects, int totalHours) {
        setId(srcClass.getId());
        setName(srcClass.getETTName());
        setRequirements(srcClass.getETTRequirements(), totalHours, existingSubjects);
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


    private void setRequirements(ETTRequirements ettRequirements, int totalHours, Map<Integer, Subject> existingSubjects) {
        this.requirements = new ArrayList<Requirement>();
        List<ETTStudy> requirementList = ettRequirements.getETTStudy();

        int totalClassHours = 0;

        for (ETTStudy r : requirementList) {
            Requirement newReq = new Requirement(r, existingSubjects);
            this.requirements.add(newReq);
            totalClassHours += newReq.getHours();
        }

        if (totalClassHours > totalHours) {
            //TODO throw exception
            System.out.println("class " + name + " has too many hours");
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
