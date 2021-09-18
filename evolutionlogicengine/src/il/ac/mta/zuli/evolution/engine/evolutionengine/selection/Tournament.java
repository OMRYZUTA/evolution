package il.ac.mta.zuli.evolution.engine.evolutionengine.selection;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.xmlparser.generated.ex2.ETTSelection;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tournament<S extends Solution> implements Selection<S> {
    // <ETT-Selection type="Tournament" configuration="pte=0.7">
    // <ETT-Elitism>5</ETT-Elitism>
    // </ETT-Selection>

    private double PTE; //Predefined tournament equalizer , between 0-1
    private int elitism;
    private final int populationSize;
    private final Random random;

    public Tournament(ETTSelection ettSelection, int populationSize) {
        this.populationSize = populationSize;
        parseConfiguration(ettSelection);

        if (ettSelection.getETTElitism() != null) {
            setElitism(ettSelection.getETTElitism());
        }// else elitism is initialized to zero anyway
        random = new Random();
    }

    public Tournament(double pte, int populationSize, int elitism) {
        this.populationSize = populationSize;
        setPTE(pte);
        setElitism(elitism);
        random = new Random();
    }

    public void setPTE(double pte) {
        //between 0-1
        if (pte >= 0 && pte <= 1) {
            this.PTE = pte;
        } else {
            throw new ValidationException("Invalid PTE value :" + pte + ". Must be between 0-1");
        }
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

        double num = Double.parseDouble(configuration.substring(index + 1));

        setPTE(num);
    }

    @Override
    public List<S> select(List<S> solutions) {
        S parent1, parent2, higherScoreParent, lowerScoreParent;
        List<S> selectedParents = new ArrayList<>();
        int populationSize = solutions.size();

        while (selectedParents.size() < populationSize) {
            parent1 = randomlySelectParent(solutions);
            parent2 = randomlySelectParent(solutions);

            if (parent1.getTotalFitnessScore() > parent2.getTotalFitnessScore()) {
                higherScoreParent = parent1;
                lowerScoreParent = parent2;
            } else {
                higherScoreParent = parent2;
                lowerScoreParent = parent1;
            }
            Double randomNum = random.nextDouble();
            if (randomNum >= PTE) {
                selectedParents.add(higherScoreParent);
            } else {
                selectedParents.add(lowerScoreParent);
            }
        }
        return selectedParents;
    }

    private S randomlySelectParent(List<S> solutions) {
        int randomIndex = new Random().nextInt(solutions.size());
        return solutions.get(randomIndex);
    }

    public void setElitism(int elitism) {
        if (elitism >= 0 && elitism <= populationSize) {
            this.elitism = elitism;
        } else {
            throw new ValidationException("number of elitism given : " + elitism + " is out of range");
        }
    }

    @Override
    public String getConfiguration() {
        return String.format("PTE = %f", PTE);
    }

    @Override
    public int getElitism() {
        return elitism;
    }
}
