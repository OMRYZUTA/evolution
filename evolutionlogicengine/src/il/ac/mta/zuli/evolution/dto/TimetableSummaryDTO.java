package il.ac.mta.zuli.evolution.dto;

import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

public class TimetableSummaryDTO {
    private final int ID;
    private final String uploadedBy;
    private final int days;
    private final int hours;
    private final int numOfTeachers;
    private final int numOfSubjects;
    private final int numOfClasses;
    private final int numOfSoftRules;
    private final int numOfHardRules;
    private final double bestScore;
    private final int numOfUsersSolving;

    public TimetableSummaryDTO(TimeTable timetable, double bestScore, int numOfUsers) {
        this.ID = timetable.getID();
        this.uploadedBy = timetable.getUploadedBy();
        this.days = timetable.getDays();
        this.hours = timetable.getHours();
        this.numOfTeachers = timetable.getNumOfTeachers();
        this.numOfSubjects = timetable.getNumOfSubjects();
        this.numOfClasses = timetable.getNumOfClasses();
        this.numOfSoftRules = timetable.getNumOfSoftRules();
        this.numOfHardRules = timetable.getNumOfHardRules();
        this.bestScore = bestScore;
        this.numOfUsersSolving = numOfUsers;
    }
}
