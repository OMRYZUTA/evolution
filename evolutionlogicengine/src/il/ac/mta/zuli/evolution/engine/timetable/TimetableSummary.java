package il.ac.mta.zuli.evolution.engine.timetable;

public class TimetableSummary {
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

    public TimetableSummary(String uploadedBy,
                            int days,
                            int hours,
                            int numOfTeachers,
                            int numOfSubjects,
                            int numOfClasses,
                            int numOfSoftRules,
                            int numOfHardRules,
                            double maxFitnessScore,
                            int numOfUsersSolving) {
        this.uploadedBy = uploadedBy;
        this.days = days;
        this.hours = hours;
        this.numOfTeachers = numOfTeachers;
        this.numOfSubjects = numOfSubjects;
        this.numOfClasses = numOfClasses;
        this.numOfSoftRules = numOfSoftRules;
        this.numOfHardRules = numOfHardRules;
        this.maxFitnessScore = maxFitnessScore;
        this.numOfUsersSolving = numOfUsersSolving;
    }

    public TimetableSummary(String uploadedBy,
                            int days,
                            int hours,
                            int numOfTeachers,
                            int numOfSubjects,
                            int numOfClasses,
                            int numOfSoftRules,
                            int numOfHardRules) {
        this.uploadedBy = uploadedBy;
        this.days = days;
        this.hours = hours;
        this.numOfTeachers = numOfTeachers;
        this.numOfSubjects = numOfSubjects;
        this.numOfClasses = numOfClasses;
        this.numOfSoftRules = numOfSoftRules;
        this.numOfHardRules = numOfHardRules;
    }

    public void setMaxFitnessScore(double maxFitnessScore) {
        this.maxFitnessScore = maxFitnessScore;
    }

    public void setNumOfUsersSolving(int numOfUsersSolving) {
        this.numOfUsersSolving = numOfUsersSolving;
    }
}
