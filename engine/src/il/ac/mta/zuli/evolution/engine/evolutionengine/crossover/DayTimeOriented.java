package il.ac.mta.zuli.evolution.engine.evolutionengine.crossover;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class DayTimeOriented<S extends Solution> implements Crossover<S> {
    private final int days;
    private final int hours;
    private int numOfCuttingPoints;
    private List<Integer> cuttingPoints; //indices for cutting points in solution

    public DayTimeOriented(int numOfCuttingPoints, int days, int hours) throws Exception {
        // no need to check validity for days and hours, since they come from timetable
        this.days = days;
        this.hours = hours;
        setNumOfCuttingPoints(numOfCuttingPoints);
    }

    @Override
    public List<S> crossover(List<S> selectedParents) {
        if (selectedParents.size() == 0) {
            throw new RuntimeException("parent-generation is empty");
        }
        if (selectedParents.size() == 1) {
            return selectedParents;
        }

        // A. randomly generate numOfCuttingPoints cutting points
        randomlyGenerateCuttingPoints();

        // B. organize each solution as 2-dimension array D*H
        List<List<List<Quintet>>> selectedSolutionsAsMatrix = new ArrayList<>();

        for (S solution : selectedParents) {
            selectedSolutionsAsMatrix.add(convertSolutionToMatrixDH(solution));
        }

        // C. randomly select 2 parents to "mate" and remove them from the pool of parents
        // for every 2-parents-couple apply crossoverBetween2Parents()
        // add the 2 returned babies to the baby-collection - return baby collection
        List<TimeTableSolution> newGeneration = new ArrayList<>();
        //flattening-back from the hierarchy of solutionMatrix to List<Quintets> field in TimeTablesolution
        //List<Quintet> quintets = selectedSolutionsAsMatrix.get(0).stream().flatMap(Collection::stream).collect(Collectors.toList());
        //newGeneration.add(new TimeTableSolution(quintets));
        List<List<Quintet>> parent1 = null;
        List<List<Quintet>> parent2 = null;
        int randomIndex;

        while (selectedSolutionsAsMatrix.size() >= 2) {
            parent1 = randomlySelectParent(selectedSolutionsAsMatrix);
            removeParentFromPoolOfParents(selectedSolutionsAsMatrix, parent1);

            parent2 = randomlySelectParent(selectedSolutionsAsMatrix);
            removeParentFromPoolOfParents(selectedSolutionsAsMatrix, parent2);

            //crossoverBetween2Parents(parent1, parent2);
        }

        //return newGeneration;
        return null;
    }

    private void removeParentFromPoolOfParents(List<List<List<Quintet>>> selectedSolutionsAsMatrix,
                                               List<List<Quintet>> parent) {
        Iterator<List<List<Quintet>>> itr = selectedSolutionsAsMatrix.iterator();
        while (itr.hasNext()) {
            List<List<Quintet>> inner = itr.next();
            if (inner.equals(parent)) {
                itr.remove();
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

    private List<S> crossoverBetween2Parents(S s1, S s2) {
        if (!(s1 instanceof TimeTableSolution) || !(s2 instanceof TimeTableSolution)) {
            throw new RuntimeException("solution must be TimeTableSolution");
        }

        TimeTableSolution ttSolution1 = (TimeTableSolution) s1;
        TimeTableSolution ttSolution2 = (TimeTableSolution) s2;

        Collections.sort(ttSolution1.getSolutionQuintets(), Comparator.comparing(Quintet::getDay)
                .thenComparing(Quintet::getHour));

        Collections.sort(ttSolution2.getSolutionQuintets(), Comparator.comparing(Quintet::getDay)
                .thenComparing(Quintet::getHour));

        //TODO complete

        return null;
    }

    private void setNumOfCuttingPoints(int numOfCuttingPoints) throws Exception {
        if (numOfCuttingPoints >= 0) {
            this.numOfCuttingPoints = numOfCuttingPoints;
        } else {
            throw new Exception("number of cutting points must be a positive integer");
        }
    }

    public int getNumOfCuttingPoints() {
        return numOfCuttingPoints;
    }

    public List<Integer> getCuttingPoints() {
        return Collections.unmodifiableList(cuttingPoints);
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

        cuttingPoints = new ArrayList<>(tempSetOfPoints);
        Collections.sort(cuttingPoints);
    }

    @Override
    public String toString() {
        return "crossover: " + this.getClass().getSimpleName() +
                "numOfCuttingPoints=" + numOfCuttingPoints;
    }
}
