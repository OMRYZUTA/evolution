package il.ac.mta.zuli.evolution.engine.timetable;

public class TimetableSummary {
    private final int ID;
    private final String uploadedBy;
    private final int days;
    private final int hours;
    private final int numOfTeachers;
    private final int numOfSubjects;
    private final int numOfClasses;
    private final int numOfSoftRules;
    private final int numOfHardRules;
    private double maxFitnessScore;
    private int numOfUsersSolving;

    public TimetableSummary(TimeTable timeTable) {
        this.ID = timeTable.getID();
        this.uploadedBy = timeTable.getUploadedBy();
        this.days = timeTable.getDays();
        this.hours = timeTable.getHours();
        this.numOfTeachers = timeTable.getNumOfTeachers();
        this.numOfSubjects = timeTable.getNumOfSubjects();
        this.numOfClasses = timeTable.getNumOfClasses();
        this.numOfSoftRules = timeTable.getNumOfSoftRules();
        this.numOfHardRules = timeTable.getNumOfHardRules();

        //after c'tor we'll need to set maxFitness and numOfUsersSolving
    }

    public void setMaxFitnessScore(double maxFitnessScore) {
        this.maxFitnessScore = maxFitnessScore;
    }

    public void setNumOfUsersSolving(int numOfUsersSolving) {
        this.numOfUsersSolving = numOfUsersSolving;
    }
}
