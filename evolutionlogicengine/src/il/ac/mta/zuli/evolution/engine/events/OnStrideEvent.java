package il.ac.mta.zuli.evolution.engine.events;

import il.ac.mta.zuli.evolution.dto.GenerationStrideScoreDTO;

public class OnStrideEvent extends Event {
    private final int generation;
    private final GenerationStrideScoreDTO generationStrideScoreDTO;

    public OnStrideEvent(String message, int generation, GenerationStrideScoreDTO generationStrideScoreDTO) {
        super(message);
        this.generation = generation;
        this.generationStrideScoreDTO = generationStrideScoreDTO;
    }

    public int getGeneration() {
        return generation;
    }

    public GenerationStrideScoreDTO getGenerationStrideScoreDTO() {
        return generationStrideScoreDTO;
    }
}
