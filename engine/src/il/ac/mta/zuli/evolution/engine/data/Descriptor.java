package il.ac.mta.zuli.evolution.engine.data;

import il.ac.mta.zuli.evolution.engine.data.generated.ETTDescriptor;

//every ctor will throw an exception all the way up (setters will validate)
//order of ctors - for example, subjects to be created before teachers

public class Descriptor {
    private TimeTable timeTable = null;
    private EngineSettings engine = null;
    /* pre-initialized instance of the singleton */
    private static Descriptor instance = null;

    /* Access point to the unique instance of the singleton */
    public static Descriptor getInstance() {
        if (instance == null) {
            synchronized (Descriptor.class) {
                instance = new Descriptor();
            }
        }

        return instance;
    }

    public void setDescriptor(ETTDescriptor d) {
        if ((timeTable == null) && (engine == null)) {
            try {
                timeTable = new TimeTable(d.getETTTimeTable());
                engine = new EngineSettings(d.getETTEvolutionEngine());
            } catch (Exception e) {
                //change later to the exception relevant to me - delete later
            }
        } else {
            //only if received another valid file we want to overwrite the previous descriptor instance
            TimeTable tempTimeTable = null;
            EngineSettings tempEngineSetting = null;
            try {
                tempTimeTable = new TimeTable(d.getETTTimeTable());
                tempEngineSetting = new EngineSettings(d.getETTEvolutionEngine());
            } catch (Exception e) {
                //change later to the exception relevant to me - delete later
            }
            timeTable = tempTimeTable;
            engine = tempEngineSetting;
        }
    }

    @Override
    public String toString() {
        return "Descriptor{" +
                "timeTable=" + timeTable +
                ", engine=" + engine +
                '}';
    }

    public TimeTable getTimeTable() {
        return timeTable;
    }


    public EngineSettings getEngine() {
        return engine;
    }
}
