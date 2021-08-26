package il.ac.mta.zuli.evolution.engine.evolutionengine.crossover;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class Crossover<S extends Solution> implements CrossoverInterface<S> {
    protected final int days;
    protected final int hours;
    protected final TimeTable timeTable;
    protected int numOfCuttingPoints;
    protected List<Integer> cuttingPointsIndices;

    public Crossover(int numOfCuttingPoints, @NotNull TimeTable timeTable) {
        this.timeTable = timeTable;
        this.days = timeTable.getDays();
        this.hours = timeTable.getHours();
        setNumOfCuttingPoints(numOfCuttingPoints);
    }

    protected List<List<List<Quintet>>> crossoverBetween2Parents(List<List<Quintet>> parent1, List<List<Quintet>> parent2) {
        List<List<List<Quintet>>> twoNewMatrix = new ArrayList<>(2);
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


        return twoNewMatrix;
    }

    //#region cuttingPoint related methods:
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

    protected void randomlyGenerateCuttingPoints() {
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

    //#endregion

    //#region quintet-list to matrix and back methods:
    protected List<List<Quintet>> convertSolutionToMatrixDH(S solution) {
        if (!(solution instanceof TimeTableSolution)) {
            throw new RuntimeException("solution must be TimeTableSolution");
        }

        TimeTableSolution timeTableSolution = (TimeTableSolution) solution;

        List<Quintet> solutionQuintets = timeTableSolution.getSolutionQuintets();

        // Array D*H length (instead of matrix) the index is: (hour * DAYS) + day (zero based)
        // each element in the array is a collection of quintets

        return convertQuintetListToMatrix(solutionQuintets);
    }

    @NotNull
    protected List<List<Quintet>> convertQuintetListToMatrix(List<Quintet> solutionQuintets) {
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
    protected List<List<Quintet>> createEmptyDHMatrix() {
        // Array D*H length (instead of matrix) the index is: (hour * DAYS) + (day - 1)
        // each element in the array is a collection of quintets
        List<List<Quintet>> solutionMatrix = new ArrayList<>(days * hours);

        for (int i = 0; i < days * hours; i++) {
            solutionMatrix.add(null);
        }

        return solutionMatrix;
    }

    protected List<TimeTableSolution> convertMatrixToSolutions(List<List<Quintet>> child1, List<List<Quintet>> child2) {
        List<TimeTableSolution> twoNewSolutions = new ArrayList<>(2);

        twoNewSolutions.add(convertSingleMatrixToSolution(child1));
        twoNewSolutions.add(convertSingleMatrixToSolution(child2));

        return twoNewSolutions;
    }

    protected TimeTableSolution convertSingleMatrixToSolution(List<List<Quintet>> quintetMatrix) {
        List<Quintet> quintets = flattenSolutionMatrix(quintetMatrix);

        return new TimeTableSolution(quintets, timeTable);
    }

    @NotNull
    protected List<Quintet> flattenSolutionMatrix(List<List<Quintet>> child1) {
        List<Quintet> quintets = new ArrayList<>();

        for (List<Quintet> quintetCollection : child1) {
            if (quintetCollection != null) {
                quintets.addAll(quintetCollection);
            }
        }
        return quintets;
    }

    //#endregion
}
