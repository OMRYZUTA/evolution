package il.ac.mta.zuli.evolution.engine.evolutionengine.mutation;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;

public class Flipping<S extends Solution> implements Mutation<S> {
    double probability;
    int maxTupples;
    ComponentName component;

    public Flipping(double probability, int maxTupples, ComponentName component) throws Exception {
        setProbability(probability);
        this.maxTupples = maxTupples;
        this.component = component;
    }

    @Override
    public void mutate(S solution) {
        //TODO implement
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
    public String getConfiguration() {
        return String.format("max tuples = %d, Component = %s", maxTupples, component.toString());
    }

    @Override
    public double getProbability() {
        return probability;
    }
}
