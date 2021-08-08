package il.ac.mta.zuli.evolution.engine.evolutionengine.mutation;


import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

import java.util.ArrayList;
import java.util.List;

import static il.ac.mta.zuli.evolution.engine.utils.generateRandomNum;
import static il.ac.mta.zuli.evolution.engine.utils.generateRandomNumZeroBase;

public class Sizer<S extends Solution> implements Mutation<S> {
    double probability;
    int totalTuple;
    TimeTable timeTable;

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

        if (!(solution instanceof TimeTableSolution)) {
            throw new RuntimeException("solution must be TimeTableSolution");
        }

        TimeTableSolution timeTableSolution = (TimeTableSolution) solution;
        if (timeTableSolution.getSolutionSize() <= timeTable.getDays()) {
            return solution;
        }
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

    private S addQuintetsToSolution(TimeTableSolution solution) {
        int numOfQuintetsToAdd = generateRandomNum(1, totalTuple);
        S result = null;
        if (solution.getSolutionSize() + numOfQuintetsToAdd < timeTable.getHours() * timeTable.getDays()) {
            result = (S) solution;
        }
        else {
            List<Quintet> quintetsToAdd = new ArrayList<>();
            for (int i = 0; i < numOfQuintetsToAdd; i++) {
                quintetsToAdd.add(solution.generateRandomQuintet());
            }
            quintetsToAdd.addAll(solution.getSolutionQuintets());
            result = (S) new TimeTableSolution(quintetsToAdd,quintetsToAdd.size(),timeTable);
        }
        return result;
    }

    private S removeQuintetsFromSolution(TimeTableSolution solution) {
        int quintetsToRemove = generateRandomNum(1, totalTuple);
        S result = null;
        if (solution.getSolutionSize() - quintetsToRemove < timeTable.getDays()) {
            result = (S) solution;
        }
        for (int i = 0; i < quintetsToRemove; i++) {
            int randomQuintetToRemove = generateRandomNumZeroBase(solution.getSolutionSize());
            solution.getSolutionQuintets().remove(randomQuintetToRemove);
            result = (S) solution;
        }
        return result;
    }

    private void setProbability(double probability) {
        if (0 <= probability && probability <= 1) {
            this.probability = probability;
        } else {
            throw new ValidationException("invalid probability, should be between 0 -1 but got" + probability);
        }
    }
}