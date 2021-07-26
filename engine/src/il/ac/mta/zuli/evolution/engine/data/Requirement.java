package il.ac.mta.zuli.evolution.engine.data;

import il.ac.mta.zuli.evolution.engine.data.generated.ETTStudy;

public class Requirement {
    //required hours per subject
    private int hours;
    private int subjectID;
//    private Subject subject;

    public Requirement(ETTStudy ettRequirement) {
        setHours(ettRequirement.getHours());
        setSubjectID(ettRequirement.getSubjectId());
        //after ctor add subject using timeTable's subject map
    }

    public int getHours() {
        return hours;
    }

    private void setHours(int hours) {
        this.hours = hours;
    }

    public int getSubject() {
        return subjectID;
    }

    private void setSubjectID(int ID) {
        this.subjectID = ID;
    }

    //    public void setSubject(Subject s){
//        this.subject = s;
//    }


    @Override
    public String toString() {
        return "Requirement{" +
                "hours=" + hours +
                ", subjectID=" + subjectID +
                '}';
    }
}
