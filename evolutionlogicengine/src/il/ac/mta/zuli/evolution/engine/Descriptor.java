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
    private Thread thread; // TODO maybe the Timetable engine should keep the thread

    public Descriptor(@NotNull ETTDescriptor d) {
        // only if received another valid file we want to overwrite the previous descriptor instance
        TimeTable tempTimeTable = new TimeTable(d.getETTTimeTable());
        //only reaching here if TimeTable c'tor didn't throw exceptions
        this.timeTable = tempTimeTable;
    }

    public void setEngineSettings(EngineSettings<TimeTableSolution> newEngineSettings) {
        this.engineSettings = newEngineSettings;
    }

    public void setState(EvolutionState state) {
        this.state = state;
    }

    public void setEndPredicates(List<EndPredicate> endPredicates) {
        this.endPredicates = endPredicates;
    }

    public void setGenerationsStride(int generationsStride) {
        this.generationsStride = generationsStride;
    }

    //Ex2 & Ex2 CTOR
//    public Descriptor(@NotNull ETTDescriptor d) {
//        // only if received another valid file we want to overwrite the previous descriptor instance
//        TimeTable tempTimeTable = new TimeTable(d.getETTTimeTable());
//        EngineSettings<TimeTableSolution> tempEngineSetting = new EngineSettings<>(d.getETTEvolutionEngine(), tempTimeTable);
//
//        //only reaching here if TimeTable and EngineSettings ctors didn't throw exceptions
//        this.timeTable = tempTimeTable;
//        this.engineSettings = tempEngineSetting;
//    }

    public int getPopulationSize() {
        return this.engineSettings.getInitialPopulationSize();
    }

    public Set<Rule> getRules() {
        return this.timeTable.getRules();
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }

    public EngineSettings<TimeTableSolution> getEngineSettings() {
        return engineSettings;
    }

    @Override
    public String toString() {
        return "Descriptor{" +
                "timeTable=" + timeTable + System.lineSeparator() +
                ", engine=" + engineSettings +
                '}';
    }
}
