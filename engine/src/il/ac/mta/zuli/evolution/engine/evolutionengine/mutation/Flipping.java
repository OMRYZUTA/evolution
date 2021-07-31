package il.ac.mta.zuli.evolution.engine.evolutionengine.mutation;

import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;

public class Flipping implements Mutation<TimeTableSolution> {
    double probability;
    int maxTupples;
    ComponentName component;

    public Flipping(double probability, int maxTupples, ComponentName component) throws Exception {
        setProbability(probability);
        this.maxTupples = maxTupples;
        this.component = component;
    }

    public double getProbability() {
        return probability;
    }

    private void setProbability(double probability) throws Exception {
        if (0 <= probability && probability <= 1) {
            this.probability = probability;
        } else {
            throw new Exception("invalid probability");
        }
    }

    public int getMaxTupples() {
        return maxTupples;
    }


    public ComponentName getComponent() {
        return component;
    }

    @Override
    public String toString() {
        return "Mutation: " + this.getClass().getSimpleName() +
                "probability=" + probability +
                ", maxTupples=" + maxTupples +
                ", component=" + component;
    }

    @Override
    public void mutate(TimeTableSolution solution) {
        //TODO implement
    }
}
