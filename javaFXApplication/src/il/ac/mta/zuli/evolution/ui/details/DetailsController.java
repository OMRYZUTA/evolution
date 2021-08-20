package il.ac.mta.zuli.evolution.ui.details;

import il.ac.mta.zuli.evolution.dto.DescriptorDTO;
import il.ac.mta.zuli.evolution.ui.enginesettings.EngineSettingsController;
import il.ac.mta.zuli.evolution.ui.timetablesettings.TimetableSettingsController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

public class DetailsController {
    @FXML
    private ScrollPane ttSettingsComponent;
    @FXML
    private TimetableSettingsController ttSettingsComponentController;
    @FXML
    private ScrollPane engineSettingsComponent;
    @FXML
    private EngineSettingsController engineSettingsComponentController;

    public void setDescriptor(DescriptorDTO descriptor) {
        ttSettingsComponentController.setTimetable(descriptor.getTimeTable());
        engineSettingsComponentController.setEngineSettings(descriptor.getEngineSettings());
    }
}
