package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.engine.evolutionengine.EngineSettings;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex2.ETTDescriptor;
import org.jetbrains.annotations.NotNull;

public class Descriptor {
    private final TimeTable timeTable;
    private final EngineSettings engineSettings;

    public Descriptor(@NotNull ETTDescriptor d) {
        // only if received another valid file we want to overwrite the previous descriptor instance
        TimeTable tempTimeTable = null;
        EngineSettings tempEngineSetting = null;

        tempTimeTable = new TimeTable(d.getETTTimeTable());
        tempEngineSetting = new EngineSettings(d.getETTEvolutionEngine(), tempTimeTable);

        //only reaching here if TimeTable and EngineSettings ctors didn't throw exceptions
        this.timeTable = tempTimeTable;
        this.engineSettings = tempEngineSetting;
    }

    public int getTimeTableHours() {
        return timeTable.getHours();
    }

    public int getTimeTableDays() {
        return timeTable.getDays();
    }

    @Override
    public String toString() {
        return "Descriptor{" +
                "timeTable=" + timeTable + System.lineSeparator() +
                ", engine=" + engineSettings +
                '}';
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public EngineSettings getEngineSettings() {
        return engineSettings;
    }
}
