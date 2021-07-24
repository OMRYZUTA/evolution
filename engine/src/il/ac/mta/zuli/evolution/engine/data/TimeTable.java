package il.ac.mta.zuli.evolution.engine.data;

import il.ac.mta.zuli.evolution.engine.data.generated.ETTTimeTable;

import java.util.List;
import java.util.Map;

public class TimeTable {
    private final int days;
    private final int hours;
    private Map<Integer, Teacher> teachers;
    private Map<Integer, Subject> subjects;
    private Map<Integer, SchoolClass> schoolClasses;
    private List<Rule> rules;

    public TimeTable(ETTTimeTable tt) {
        this.days = tt.getDays();
        this.hours = tt.getHours();
        //continue - delete later
    }

    @Override
    public String toString() {
        return "TimeTable{" +
                "days=" + days +
                ", hours=" + hours +
                '}';
    }
}
