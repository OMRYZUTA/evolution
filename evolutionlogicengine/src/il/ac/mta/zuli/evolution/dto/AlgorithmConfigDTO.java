package il.ac.mta.zuli.evolution.dto;

import il.ac.mta.zuli.evolution.engine.TimetableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EngineSettings;
import il.ac.mta.zuli.evolution.engine.predicates.EndPredicate;

import java.util.List;

public class AlgorithmConfigDTO {
    private final int timetableID;
    private final int stride;
    private final EndPredicatesDTO endPredicates;
    private final EngineSettingsDTO engineSettings;

    public AlgorithmConfigDTO(int timetableID,
                              int stride,
                              List<EndPredicate> endPredicates,
                              EngineSettings<TimetableSolution> engineSettings) {
        this.timetableID = timetableID;
        this.stride = stride;
        this.endPredicates = new EndPredicatesDTO(endPredicates);
        this.engineSettings = new EngineSettingsDTO(engineSettings);
    }

    public int getTimetableID() {
        return timetableID;
    }

    public int getStride() {
        return stride;
    }

    public EngineSettingsDTO getEngineSettings() {
        return engineSettings;
    }

    public EndPredicatesDTO getEndPredicates() {
        return endPredicates;
    }
}


