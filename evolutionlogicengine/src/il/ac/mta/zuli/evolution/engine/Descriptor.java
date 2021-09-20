package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.engine.evolutionengine.EngineSettings;
import il.ac.mta.zuli.evolution.engine.rules.Rule;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex2.ETTDescriptor;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class Descriptor {
    private final TimeTable timeTable;
    private EngineSettings<TimeTableSolution> engineSettings;

    public Descriptor(@NotNull ETTDescriptor d) {
        // only if received another valid file we want to overwrite the previous descriptor instance
        TimeTable tempTimeTable = new TimeTable(d.getETTTimeTable());
        EngineSettings<TimeTableSolution> tempEngineSetting = new EngineSettings<>(d.getETTEvolutionEngine(), tempTimeTable);

        //only reaching here if TimeTable and EngineSettings ctors didn't throw exceptions
        this.timeTable = tempTimeTable;
        this.engineSettings = tempEngineSetting;
    }

    public int getPopulationSize() {
        return this.engineSettings.getInitialPopulationSize();
    }

    public Set<Rule> getRules() {
        return this.timeTable.getRules();
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

    public EngineSettings<TimeTableSolution> getEngineSettings() {
        return engineSettings;
    }

    public void setValidatedEngineSettings(EngineSettings<TimeTableSolution> newEngineSettings) {
        this.engineSettings = newEngineSettings;
    }
}