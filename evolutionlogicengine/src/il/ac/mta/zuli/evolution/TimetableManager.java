package il.ac.mta.zuli.evolution;

import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimetableManager {
    private final List<TimeTable> timetables; //for each valid xml uploaded, add a timetable to the collection

    public TimetableManager() {
        timetables = new ArrayList<>();
    }

    public List<TimeTable> getTimetables() {
        return Collections.unmodifiableList(timetables);
    }
}
