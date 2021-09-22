package il.ac.mta.zuli.evolution;

import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimetableManager {
//    the index in the array will serve as the ID of the timetable
private final List<TimeTable> timetables; //for each valid xml uploaded, add a timetable to the collection

    public TimetableManager() {
        timetables = new ArrayList<>();
    }

    public List<TimeTable> getTimetables() {
        return Collections.unmodifiableList(timetables);
    }
}
