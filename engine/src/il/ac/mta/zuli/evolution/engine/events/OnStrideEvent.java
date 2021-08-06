package il.ac.mta.zuli.evolution.engine.events;

import il.ac.mta.zuli.evolution.dto.GenerationStrideScoreDTO;

import java.awt.event.ActionEvent;

public class OnStrideEvent extends ActionEvent {


    private GenerationStrideScoreDTO generationStrideScoreDTO;
    public OnStrideEvent(Object source, int id, String command, GenerationStrideScoreDTO generationStrideScoreDTO){
        super(source, id, command);
        this.generationStrideScoreDTO =generationStrideScoreDTO;
    }
    public OnStrideEvent(Object source, int id, String command) {
        super(source, id, command);
    }

    public OnStrideEvent(Object source, int id, String command, int modifiers) {
        super(source, id, command, modifiers);
    }
    public GenerationStrideScoreDTO getGenerationStrideScoreDTO() {
        return generationStrideScoreDTO;
    }

    public OnStrideEvent(Object source, int id, String command, long when, int modifiers) {
        super(source, id, command, when, modifiers);
    }
}
