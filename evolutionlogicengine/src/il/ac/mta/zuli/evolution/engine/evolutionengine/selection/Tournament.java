package il.ac.mta.zuli.evolution.engine.evolutionengine.selection;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tournament<S extends Solution> implements Selection<S> {
    private double PTE; //Predefined tournament equalizer , between 0-1
    private int elitism;
    private final int populationSize;
    private final Random random;

    public Tournament(double pte, int populationSize, int elitism) {
        this.populationSize = populationSize;
        setPTE(pte);
        setElitism(elitism);
        random = new Random();
    }

    @Override
    public List<S> select(List<S> solutions) {
        S parent1, parent2, higherScoreParent, lowerScoreParent;
        List<S> selectedParents = new ArrayList<>();
        int populationSize = solutions.size();

        while (selectedParents.size() < populationSize) {
            parent1 = randomlySelectParent(solutions);
            parent2 = randomlySelectParent(solutions);

            if (parent1.getFitnessScore() > parent2.getFitnessScore()) {
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

    public void setPTE(double pte) {
        //between 0-1
        if (pte >= 0 && pte <= 1) {
            this.PTE = pte;
        } else {
            throw new ValidationException("Invalid PTE value :" + pte + ". Must be between 0-1");
        }
    }

    public void setElitism(int elitism) {
        if (elitism >= 0 && elitism <= populationSize) {
            this.elitism = elitism;
        } else {
            throw new ValidationException("number of elitism given : " + elitism + " is out of range");
        }
    }

    @Override
    public String getSelectionType() {
        return getClass().getSimpleName();
    }

    @Override
    public int getElitism() {
        return elitism;
    }

    public double getPTE() {
        return PTE;
    }

    @Override
    public String getConfiguration() {
        return String.format("PTE = %f", PTE);
    }
}
