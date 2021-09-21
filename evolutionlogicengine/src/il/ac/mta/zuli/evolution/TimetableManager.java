package il.ac.mta.zuli.evolution;

import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

import java.util.List;

public class TimetableManager {
    private final List<TimeTable> timetables;

    public TimetableManager(List<TimeTable> timetables) {
        this.timetables = timetables;
    }
}
