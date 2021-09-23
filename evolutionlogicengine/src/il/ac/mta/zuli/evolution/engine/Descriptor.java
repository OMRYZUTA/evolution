package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.engine.evolutionengine.EngineSettings;
import il.ac.mta.zuli.evolution.engine.predicates.EndPredicate;
import il.ac.mta.zuli.evolution.engine.rules.Rule;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex2.ETTDescriptor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

//in Ex. 3 descriptor is a run of the algorithm for a certain problem
public class Descriptor {
    //in Ex2 Descriptor==the xml file, in Ex3 only Timetable == file
    private final TimeTable timeTable; //the problem
    private EngineSettings<TimeTableSolution> engineSettings; //the configuration

    //Ex3 additions to Descriptor:
    private EvolutionState state;
    private List<EndPredicate> endPredicates;
    private int generationsStride;
    private Thread thread;

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
