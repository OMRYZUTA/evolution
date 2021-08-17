package il.ac.mta.zuli.evolution.engine.evolutionengine.crossover;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.EmptyCollectionException;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DayTimeOriented<S extends Solution> implements Crossover<S> {
    private final int days;
    private final int hours;
    private final TimeTable timeTable;
    private int numOfCuttingPoints;
    private List<Integer> cuttingPointsIndices;

    public DayTimeOriented(int numOfCuttingPoints, @NotNull TimeTable timeTable) {
        this.timeTable = timeTable;
        this.days = timeTable.getDays();
        this.hours = timeTable.getHours();
        setNumOfCuttingPoints(numOfCuttingPoints);
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

            newGeneration.addAll(crossoverBetween2Parents(parent1, parent2));
        }

        // if there is one parent left, need to add it to new generations
        if (selectedSolutionsAsMatrix.size() == 1) {
            List<Quintet> quintets = flattenSolutionMatrix(selectedSolutionsAsMatrix.get(0));
            newGeneration.add(new TimeTableSolution(quintets, timeTable));
        }

        return newGeneration;
    }

    @NotNull
    private List<List<List<Quintet>>> organizeSolutionsAsDayHourMatrix(List<S> selectedParents) {
        List<List<List<Quintet>>> selectedSolutionsAsMatrix = new ArrayList<>();

        for (S solution : selectedParents) {
            selectedSolutionsAsMatrix.add(convertSolutionToMatrixDH(solution));
        }
        return selectedSolutionsAsMatrix;
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

    private List<List<Quintet>> randomlySelectParent(List<List<List<Quintet>>> selectedSolutionsAsMatrix) {
        int randomIndex = new Random().nextInt(selectedSolutionsAsMatrix.size());

        return selectedSolutionsAsMatrix.get(randomIndex);
    }

    private List<List<Quintet>> convertSolutionToMatrixDH(S solution) {
        if (!(solution instanceof TimeTableSolution)) {
            throw new RuntimeException("solution must be TimeTableSolution");
        }

        TimeTableSolution timeTableSolution = (TimeTableSolution) solution;

        List<Quintet> solutionQuintets = timeTableSolution.getSolutionQuintets();

        // Array D*H length (instead of matrix) the index is: (hour * DAYS) + day (zero based)
        // each element in the array is a collection of quintets

        return fillQuintetsToMatrix(solutionQuintets);
    }

    @NotNull
    private List<List<Quintet>> fillQuintetsToMatrix(List<Quintet> solutionQuintets) {
        List<List<Quintet>> solutionMatrix = createEmptyDHMatrix();

        for (Quintet quintet : solutionQuintets) {
            int hourIndex = quintet.getHour();
            int dayIndex = quintet.getDay().getValue() - 1;
            int i = hourIndex * days + dayIndex;
            if (solutionMatrix.get(i) == null) {
                solutionMatrix.set(i, new ArrayList<>());
            }

            (solutionMatrix.get(i)).add(quintet);
        }

        return solutionMatrix;
    }


    @NotNull
    private List<List<Quintet>> createEmptyDHMatrix() {
        // Array D*H length (instead of matrix) the index is: (hour * DAYS) + (day - 1)
        // each element in the array is a collection of quintets
        List<List<Quintet>> solutionMatrix = new ArrayList<>(days * hours);

        for (int i = 0; i < days * hours; i++) {
            solutionMatrix.add(null);
        }

        return solutionMatrix;
    }

    private List<TimeTableSolution> crossoverBetween2Parents(List<List<Quintet>> parent1, List<List<Quintet>> parent2) {
        List<TimeTableSolution> twoNewSolutions = new ArrayList<>(2);
        List<List<Quintet>> child1 = new ArrayList<>();
        List<List<Quintet>> child2 = new ArrayList<>();
        Iterator<Integer> cuttingPointsItr = cuttingPointsIndices.iterator();
        int cuttingPoint = cuttingPointsItr.next();
        boolean parent1ToChild1 = true;

        for (int i = 0; i < days * hours; i++) {
            if (i == cuttingPoint) {
                // switch between parents and children
                parent1ToChild1 = !parent1ToChild1;
                if (cuttingPointsItr.hasNext()) {
                    cuttingPoint = cuttingPointsItr.next();
                }
            }
            if (parent1ToChild1) {
                child1.add(parent1.get(i));
                child2.add(parent2.get(i));
            } else {
                child2.add(parent1.get(i));
                child1.add(parent2.get(i));
            }
        }

        //flattening-back from the hierarchy of solutionMatrix to List<Quintets> field in TimeTablesolution
        convertMatrixToSolutions(twoNewSolutions, child1, child2);

        return twoNewSolutions;
    }

    private void convertMatrixToSolutions(List<TimeTableSolution> twoNewSolutions, List<List<Quintet>> child1, List<List<Quintet>> child2) {
        List<Quintet> quintets = flattenSolutionMatrix(child1);
        TimeTableSolution tempSolution = new TimeTableSolution(quintets, timeTable);
        twoNewSolutions.add(tempSolution);

        quintets = flattenSolutionMatrix(child2);
        tempSolution = new TimeTableSolution(quintets, timeTable);
        twoNewSolutions.add(tempSolution);
    }

    @NotNull
    private List<Quintet> flattenSolutionMatrix(List<List<Quintet>> child1) {
        List<Quintet> quintets = new ArrayList<>();

        for (List<Quintet> quintetCollection : child1) {
            if (quintetCollection != null) {
                quintets.addAll(quintetCollection);
            }
        }
        return quintets;
    }

    private void setNumOfCuttingPoints(int numOfCuttingPoints) {
        if (numOfCuttingPoints > 0 && numOfCuttingPoints < days * hours) {
            this.numOfCuttingPoints = numOfCuttingPoints;
        } else {
            throw new ValidationException("Invalid number of cutting points, must be between 1 -" + days * hours);
        }
    }

    public int getNumOfCuttingPoints() {
        return numOfCuttingPoints;
    }

    private void randomlyGenerateCuttingPoints() {
        Set<Integer> tempSetOfPoints = new HashSet<>();

        while (tempSetOfPoints.size() < numOfCuttingPoints) {
            //we want random points from 1 to total size of solution (D*H)
            if (days * hours > 1) {
                tempSetOfPoints.add((new Random().nextInt((days * hours) - 1)) + 1);
            } else {
                tempSetOfPoints.add(1);
            }
        }

        cuttingPointsIndices = new ArrayList<>(tempSetOfPoints);
        Collections.sort(cuttingPointsIndices);
    }

    @Override
    public String toString() {
        return "crossover: " + this.getClass().getSimpleName() +
                "numOfCuttingPoints=" + numOfCuttingPoints;
    }
}