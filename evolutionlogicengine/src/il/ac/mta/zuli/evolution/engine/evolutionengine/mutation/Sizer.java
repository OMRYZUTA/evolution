package il.ac.mta.zuli.evolution.engine.evolutionengine.mutation;


import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimetableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static il.ac.mta.zuli.evolution.engine.EngineUtils.generateRandomNum;
import static il.ac.mta.zuli.evolution.engine.EngineUtils.generateRandomNumZeroBase;

public class Sizer<S extends Solution> implements Mutation<S> {
    double probability;
    final int totalTuple; // number of tuples to add or remove
    final TimeTable timeTable;

    public Sizer(double probability, int totalTuple, TimeTable timeTable) {
        setProbability(probability);
        this.totalTuple = totalTuple;
        this.timeTable = timeTable;
    }

    public int getTotalTuple() {
        return totalTuple;
    }

    @Override
    public S mutate(S solution) {
        double randomProbability = Math.random();

        if (randomProbability > probability) {
            return solution;
        }

        if (!(solution instanceof TimetableSolution)) {
            throw new RuntimeException("solution must be TimeTableSolution");
        }

        TimetableSolution timeTableSolution = (TimetableSolution) solution;
        S mutatedSolution;

        if (totalTuple < 0) {
            mutatedSolution = removeQuintetsFromSolution(timeTableSolution);
        } else {
            mutatedSolution = addQuintetsToSolution(timeTableSolution);
        }

        return mutatedSolution;
    }

    @Override
    public String getConfiguration() {
        return String.format("Total Tuple = %d ", totalTuple);
    }

    @Override
    public double getProbability() {
        return probability;
    }

    private S addQuintetsToSolution(TimetableSolution solution) {
        int numOfQuintetsToAdd = generateRandomNum(1, totalTuple);
        S result = null;

        if (solution.getSolutionSize() + numOfQuintetsToAdd > timeTable.getHours() * timeTable.getDays()) {
            result = (S) solution;
        } else {
            Set<Quintet> quintetsWithAddition = new HashSet<>(solution.getSolutionQuintets());

            while (quintetsWithAddition.size() < solution.getSolutionSize() + numOfQuintetsToAdd) {
                quintetsWithAddition.add(solution.generateRandomQuintet());
            }

            result = (S) new TimetableSolution(new ArrayList<>(quintetsWithAddition), timeTable);
        }

        return result;
    }

    private S removeQuintetsFromSolution(TimetableSolution solution) {
        int quintetsToRemove = generateRandomNum(1, -totalTuple); // -total tuple because in this case total tuple will be negative
        S result = null;

        if (solution.getSolutionSize() - quintetsToRemove < timeTable.getDays()) {
            result = (S) solution;
        } else {
            List<Quintet> solutionQuintets = new ArrayList<>(solution.getSolutionQuintets());

            for (int i = 0; i < quintetsToRemove; i++) {
                int randomQuintetToRemove = generateRandomNumZeroBase(solutionQuintets.size());
                solutionQuintets.remove(randomQuintetToRemove);
            }

            result = (S) new TimetableSolution(solutionQuintets, timeTable);
        }

        return result;
    }

    private void setProbability(double probability) {
        if (0 <= probability && probability <= 1) {
            this.probability = probability;
        } else {
            throw new ValidationException("probability must be between 0 -1, invalid value: " + probability);
        }
    }
}