package il.ac.mta.zuli.evolution.dto;

public class DescriptorDTO {
    private final TimeTableDTO timeTable;
    private final EngineSettingsDTO engineSettings;

    public DescriptorDTO(TimeTableDTO timeTable, EngineSettingsDTO engineSettings) {
        this.timeTable = timeTable;
        this.engineSettings = engineSettings;
    }

    public TimeTableDTO getTimeTable() {
        return timeTable;
    }

    public EngineSettingsDTO getEngineSettings() {
        return engineSettings;
    }
}
