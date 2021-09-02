package il.ac.mta.zuli.evolution.engine.evolutionengine.selection;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex2.ETTSelection;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Truncation<S extends Solution> implements Selection<S> {
    private int topPercent;
    private int elitism;
    private final int populationSize;

    public Truncation(ETTSelection ettSelection, int populationSize) {
        parseConfiguration(ettSelection);
        this.populationSize = populationSize;
        if (ettSelection.getETTElitism() != null) {
            setElitism(ettSelection.getETTElitism());
        }// else elitism is initialize to zero anyway
    }

    public Truncation(int topPercent, int populationSize, int elitism) {
        this.populationSize = populationSize;
        setTopPercent(topPercent);
        setElitism(elitism);

    }

    private void parseConfiguration(ETTSelection ettSelection) {
        String configuration = ettSelection.getConfiguration();

        if (configuration.length() == 0) {
            throw new ValidationException("Empty configuration ");
        }

        int index = configuration.indexOf('=');

        if (index == -1) {
            throw new ValidationException("missing '=' ");
        }

        int num = Integer.parseInt(configuration.substring(index + 1));

        setTopPercent(num);
    }



    @Override
    public List<S> select(List<S> solutions) {
        Collections.sort(solutions); //sorting by fitnessScore
        Collections.reverse(solutions); //in descending order

        int topFitnessSolutions = (int) Math.ceil(((double) topPercent * solutions.size()) / 100);
        topFitnessSolutions = Math.min(topFitnessSolutions, (populationSize - elitism));

        return solutions.stream().limit(topFitnessSolutions).collect(Collectors.toList());
    }

    public String getConfiguration() {
        return String.format("TopPercent = %d", topPercent);
    }

    @Override
    public int getElitism() {
        return elitism;
    }

    public void setElitism(int elitism) {
        if (elitism > 0 && elitism <= populationSize) {
            this.elitism = elitism;
        } else {
            throw new ValidationException("number of elitism given : " + elitism + " is out of range");
        }
    }

    private void setTopPercent(int topPercent) {
        if (topPercent >= 1 && topPercent <= 100) {
            this.topPercent = topPercent;
        } else {
            throw new RuntimeException("Invalid top percent value :" + topPercent + ". Must be between 1 - 100");
        }
    }

    @Override
    public String toString() {
        return "Selection: " + this.getClass().getSimpleName() +
                "topPercent=" + topPercent;
    }
}
