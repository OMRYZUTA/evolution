package il.ac.mta.zuli.evolution.dto;

import il.ac.mta.zuli.evolution.Constants;
import il.ac.mta.zuli.evolution.engine.TimetableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Flipping;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Mutation;
import il.ac.mta.zuli.evolution.engine.evolutionengine.mutation.Sizer;

public class MutationDTO {
    private final String type;
    private final double probability;
    private Integer maxTuples = null;
    private Integer totalTuples = null;
    private String component = null;


    public MutationDTO(Mutation<TimetableSolution> mutation) {
        this.type = mutation.getMutationType();
        this.probability = mutation.getProbability();

        if (type.equals(Constants.FLIPPING)) {
            Flipping<TimetableSolution> flipping = (Flipping<TimetableSolution>) mutation;
            this.maxTuples = flipping.getMaxTuples();
            this.component = flipping.getComponent().name();
        } else if (type.equals(Constants.SIZER)) {
            Sizer<TimetableSolution> sizer = (Sizer<TimetableSolution>) mutation;
            this.totalTuples = sizer.getTotalTuples();
        }
    }

    public String getType() {
        return type;
    }

    public double getProbability() {
        return probability;
    }

    public Integer getMaxTuples() {
        return maxTuples;
    }

    public Integer getTotalTuples() {
        return totalTuples;
    }

    public String getComponent() {
        return component;
    }

    @Override
    public String toString() {
        return "Mutation: " + type +
                ", probability " + probability;
    }
}
