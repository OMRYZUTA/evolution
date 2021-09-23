package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.dto.DescriptorDTO;
import il.ac.mta.zuli.evolution.dto.GenerationProgressDTO;
import il.ac.mta.zuli.evolution.dto.TimeTableSolutionDTO;
import il.ac.mta.zuli.evolution.engine.evolutionengine.EngineSettings;
import il.ac.mta.zuli.evolution.engine.predicates.EndPredicate;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import il.ac.mta.zuli.evolution.ui.header.HeaderController;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;

import java.util.List;
import java.util.function.Consumer;

public interface Engine {
    void setController(HeaderController controller);

    void setValidatedEngineSettings(EngineSettings settings);

    SimpleIntegerProperty getGenerationNumProperty();

    SimpleDoubleProperty getFitnessProperty();

    SimpleLongProperty getTimeProperty();

    EngineSettings getEngineSettings();

    TimeTable getTimeTable();

    DescriptorDTO getDescriptorDTO();

    void startEvolutionAlgorithm(
            List<EndPredicate> endConditions,
            int generationsStride,
            Consumer<Boolean> onSuccess,
            Consumer<Throwable> onFailure,
            Consumer<TimeTableSolutionDTO> reportBestSolution);

    void stopEvolutionAlgorithm();

    void pauseEvolutionAlgorithm();

    void resumeEvolutionAlgorithm(List<EndPredicate> endConditions,
                                  int generationsStride,
                                  Consumer<Boolean> onSuccess,
                                  Consumer<Throwable> onFailure,
                                  Consumer<TimeTableSolutionDTO> reportBestSolution);

    List<GenerationProgressDTO> getEvolutionProgress();
}
