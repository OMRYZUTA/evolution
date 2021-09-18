package il.ac.mta.zuli.evolution.engine.evolutionengine.crossover;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.EmptyCollectionException;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class DayTimeOriented<S extends Solution> extends Crossover<S> {

    public DayTimeOriented(int numOfCuttingPoints, @NotNull TimeTable timeTable) {
        super(numOfCuttingPoints, timeTable);
    }

    @Override
    public List<S> crossover(List<S> selectedParents) {
        if (selectedParents.size() == 0) {
            throw new EmptyCollectionException("Parent-generation is empty");
        }
        if (selectedParents.size() == 1) {
            return selectedParents;
        }

        randomlyGenerateCuttingPoints();

        List<List<List<Quintet>>> selectedSolutionsAsMatrix = organizeSolutionsAsDayHourMatrix(selectedParents);

        List<TimeTableSolution> newGeneration = createNewGenerationFromParents(selectedSolutionsAsMatrix);

        return (List<S>) newGeneration;
    }

    @NotNull
    private List<TimeTableSolution> createNewGenerationFromParents(List<List<List<Quintet>>> selectedSolutionsAsMatrix) {
        //  randomly select 2 parents to "mate" and remove them from the pool of parents
        // for every 2-parents-couple apply crossoverBetween2Parents()
        // add the 2 children to the new generation
        List<TimeTableSolution> newGeneration = new ArrayList<>();

        List<List<Quintet>> parent1;
        List<List<Quintet>> parent2;

        while (selectedSolutionsAsMatrix.size() >= 2) {
            parent1 = randomlySelectParent(selectedSolutionsAsMatrix);
            removeParentFromPoolOfParents(selectedSolutionsAsMatrix, parent1);

            parent2 = randomlySelectParent(selectedSolutionsAsMatrix);
            removeParentFromPoolOfParents(selectedSolutionsAsMatrix, parent2);

            List<List<List<Quintet>>> twoMatrixChildren = crossoverBetween2Parents(parent1, parent2);
            //flattening-back from the hierarchy of solutionMatrix to List<Quintets> field in TimeTablesolution
            List<TimeTableSolution> twoSolutionChildren = convertMatrixToSolutions(twoMatrixChildren.get(0), twoMatrixChildren.get(1));
            newGeneration.addAll(twoSolutionChildren);
        }

        // if there is one parent left, need to add it to new generations
        if (selectedSolutionsAsMatrix.size() == 1) {
            List<Quintet> quintets = flattenSolutionMatrix(selectedSolutionsAsMatrix.get(0));
            newGeneration.add(new TimeTableSolution(quintets, timeTable));
        }

        return newGeneration;
    }

    private List<List<Quintet>> randomlySelectParent(List<List<List<Quintet>>> selectedSolutionsAsMatrix) {
        int randomIndex = new Random().nextInt(selectedSolutionsAsMatrix.size());

        return selectedSolutionsAsMatrix.get(randomIndex);
    }

    private void removeParentFromPoolOfParents(List<List<List<Quintet>>> selectedSolutionsAsMatrix,
                                               List<List<Quintet>> parent) {
        Iterator<List<List<Quintet>>> itr = selectedSolutionsAsMatrix.iterator();

        while (itr.hasNext()) {
            List<List<Quintet>> inner = itr.next();
            if (inner.equals(parent)) {
                itr.remove();
                break;
            }
        }
    }

    @NotNull
    private List<List<List<Quintet>>> organizeSolutionsAsDayHourMatrix(List<S> selectedParents) {
        List<List<List<Quintet>>> selectedSolutionsAsMatrix = new ArrayList<>();

        for (S solution : selectedParents) {
            selectedSolutionsAsMatrix.add(convertSolutionToMatrixDH(solution));
        }
        return selectedSolutionsAsMatrix;
    }

    @Override
    public String toString() {
        return "crossover: " + this.getClass().getSimpleName() +
                "numOfCuttingPoints=" + super.getNumOfCuttingPoints();
    }
}
