package il.ac.mta.zuli.evolution.engine.evolutionengine.selection;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RouletteWheel<S extends Solution> implements Selection<S> {

    private List<Integer> scoreRangePerIndex;

    @Override
    public List<S> select(List<S> solutions) {
        List<S> selectedParents = new ArrayList<>();
        int populationSize = solutions.size();
        int solutionIndex;
        Random random = new Random();

        scoreRangePerIndex = buildScoreRangePerIndexList(solutions);

        for (int i = 0; i < populationSize; i++) {
            //scoreRangePerIndex.get() returns an index in the solutions-list
            solutionIndex = scoreRangePerIndex.get(random.nextInt(scoreRangePerIndex.size()));
            selectedParents.add(solutions.get(solutionIndex));
        }

        return selectedParents;
    }

    //this method builds an array that holds the indices of the solutions-list (solutions to select from),
    //the number of elements in this array-per-solutionIndex are in accordance with the solution's score (rounded up)
    private List<Integer> buildScoreRangePerIndexList(@NotNull List<S> solutions) {
        int solutionsSize = solutions.size();
        double sum = 0;
        double currSolutionScore;
        int elementsToAdd;
        List<Integer> scoreRangePerIndex = new ArrayList<>();

        for (int i = 0; i < solutionsSize; i++) {
            currSolutionScore = solutions.get(i).getTotalFitnessScore();
            sum += currSolutionScore; //the sum-roundedUp is the size of the new list so far
            //add as many elements as solution[i]'s score
            elementsToAdd = (int) Math.ceil(currSolutionScore);
            List<Integer> tempList = Collections.nCopies(elementsToAdd, i);
            scoreRangePerIndex.addAll((int) Math.ceil(sum), tempList);
        }

        return scoreRangePerIndex;
    }

    @Override
    public String getConfiguration() {
        //this selection has no configuration bur we still need to override the interface method
        return "-";
    }

//    an example to make things more clear:
//    if there are 3 solutions: solutions[0].score=80, solutions[1].score=20, solutions[2].score=75
//    then scoreRangePerIndex will look like this:
//    scoreRangePerIndex[0]-scoreRangePerIndex[79]: value is 0
//    scoreRangePerIndex[80]-scoreRangePerIndex[99]: value is 1
//    scoreRangePerIndex[100]-scoreRangePerIndex[174]: value is 2
}
