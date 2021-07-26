package il.ac.mta.zuli.evolution.engine.data;

import il.ac.mta.zuli.evolution.engine.data.generated.ETTDescriptor;

public class Descriptor {
    private final TimeTable timeTable;
    private final EvolutionEngine engine;

    //every ctor will throw an exception all the way up (setters will validate)
    //order of ctors - for example, subjects to be created before teachers
    public Descriptor(ETTDescriptor d) {
        this.timeTable = new TimeTable(d.getETTTimeTable());
        this.engine = new EvolutionEngine(d.getETTEvolutionEngine());
    }

    @Override
    public String toString() {
        return "Descriptor{" +
                "timeTable=" + timeTable +
                ", engine=" + engine +
                '}';
    }
}
