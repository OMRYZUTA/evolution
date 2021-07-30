package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.engine.evolutionengine.EngineSettings;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ETTDescriptor;

//every ctor will throw an exception all the way up (setters will validate)
//order of ctors - for example, subjects to be created before teachers

public class Descriptor {
    private TimeTable timeTable;
    private EngineSettings engineSettings;

    public Descriptor(ETTDescriptor d) {
        //only if received another valid file we want to overwrite the previous descriptor instance
        TimeTable tempTimeTable = null;
        EngineSettings tempEngineSetting = null;

        try {
            tempTimeTable = new TimeTable(d.getETTTimeTable());
            tempEngineSetting = new EngineSettings(d.getETTEvolutionEngine());

            this.timeTable = tempTimeTable;
            this.engineSettings = tempEngineSetting;
        } catch (Exception e) {
            //did we previously have a valid file loaded or was Descriptor empty - loadXML method will handle this
            //TODO throw exception
        }
    }

    /* Access point to the unique instance of the singleton */
   /* public static Descriptor getInstance() {
        if (instance == null) {
            synchronized (Descriptor.class) {
                instance = new Descriptor();
            }
        }
        return instance;
    }*/

    @Override
    public String toString() {
        return "Descriptor{" +
                "timeTable=" + timeTable +
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
