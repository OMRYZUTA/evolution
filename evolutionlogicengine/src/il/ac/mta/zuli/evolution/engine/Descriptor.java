package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.engine.evolutionengine.EngineSettings;
import il.ac.mta.zuli.evolution.engine.rules.Rule;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex3.ETTDescriptor;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

//in Ex. 3 descriptor is a run of the algorithm for a certain problem
public class Descriptor {
    private final TimeTable timeTable; //the problem (in Ex2 Descriptor==the xml file, in Ex3 only Timetable == file)
    private EngineSettings<TimetableSolution> engineSettings; //the configuration

    public Descriptor(@NotNull ETTDescriptor d) {
        // only if received another valid file we want to overwrite the previous descriptor instance
        //only reaching here if TimeTable c'tor didn't throw exceptions
        this.timeTable = new TimeTable(d.getETTTimeTable());
    }

    public Descriptor(TimeTable timeTable, EngineSettings engineSettings) {
        this.timeTable = timeTable;
        this.engineSettings = engineSettings;
    }

    public void setEngineSettings(EngineSettings<TimetableSolution> newEngineSettings) {
        this.engineSettings = newEngineSettings;
    }

    public int getPopulationSize() {
        return this.engineSettings.getPopulationSize();
    }

    public Set<Rule> getRules() {
        return this.timeTable.getRules();
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public EngineSettings<TimetableSolution> getEngineSettings() {
        return engineSettings;
    }

    public int getTimetableID() {
        return timeTable.getID();
    }

    @Override
    public String toString() {
        return "Descriptor{" +
                "timeTable=" + timeTable + System.lineSeparator() +
                ", engine=" + engineSettings +
                '}';
    }
}
