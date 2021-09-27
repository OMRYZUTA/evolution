package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.dto.DescriptorDTO;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EngineSettings;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

public interface Engine {
    void setValidatedEngineSettings(EngineSettings settings);

    EngineSettings getEngineSettings();

    TimeTable getTimeTable();

    DescriptorDTO getDescriptorDTO();

    void startEvolutionAlgorithm();

    void stopEvolutionAlgorithm();

    void pauseEvolutionAlgorithm();

    void resumeEvolutionAlgorithm();
}
