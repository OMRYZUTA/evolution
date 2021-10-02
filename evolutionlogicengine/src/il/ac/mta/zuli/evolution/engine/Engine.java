package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.engine.evolutionengine.EngineSettings;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

public interface Engine {
    void setValidatedEngineSettings(EngineSettings settings);

    EngineSettings getEngineSettings();

    TimeTable getTimeTable();


    void startEvolutionAlgorithm();

    void stopEvolutionAlgorithm();

    void pauseEvolutionAlgorithm();

    void resumeEvolutionAlgorithm();
}
