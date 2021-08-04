package il.ac.mta.zuli.evolution.engine.evolutionengine.crossover;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;

import java.util.*;

public class DayTimeOriented<S extends Solution> implements Crossover<S> {
    private final int days;
    private final int hours;
    private int numOfCuttingPoints;
    private final List<Integer> cuttingPoints; //indices for cutting points in solution

    public DayTimeOriented(int numOfCuttingPoints, int days, int hours) throws Exception {
        this.days = days;
        this.hours = hours;
        setNumOfCuttingPoints(numOfCuttingPoints);
        cuttingPoints = new ArrayList<>();
    }

    @Override
    public List<S> crossover(List<S> selectedParents) {
        //algo:
        // randomize cutting points
        // organize each solution as 2-dimension array D*H
        // randomly select 2 parents to "mate" and remove them from the pool of parents
        // for every 2-parents-couple apply crossoverBetween2Parents()
        // add the 2 returned babies to the baby-collection - return baby collection

        System.out.println("in daytime crossover");
        for (S solution : selectedParents) {
            List<List<Quintet>> solutionMatrix = convertSolutionToMatrixDH(solution);
        }


        return null;
    }

    private List<List<Quintet>> convertSolutionToMatrixDH(S solution) {
        //maybe cast here to TimeTablesolution
        //matrix where the columns are days and the rows are hours

        List<List<Quintet>> solutionMatrix = new ArrayList<>();
        for (int i = 0; i < hours; i++) {
            solutionMatrix.add(new ArrayList(days));
        }
        //TODO: put solution in matrix

        System.out.println("size " + solutionMatrix.size());
        return solutionMatrix;
    }

    @Override
    public List<S> crossoverBetween2Parents(S s1, S s2) {
        if (!(s1 instanceof TimeTableSolution) || !(s2 instanceof TimeTableSolution)) {
            throw new RuntimeException("solution must be TimeTableSolution");
        }

        TimeTableSolution ttSolution1 = (TimeTableSolution) s1;
        TimeTableSolution ttSolution2 = (TimeTableSolution) s2;

        Collections.sort(ttSolution1.getSolution(), Comparator.comparing(Quintet::getDay)
                .thenComparing(Quintet::getHour));

        Collections.sort(ttSolution2.getSolution(), Comparator.comparing(Quintet::getDay)
                .thenComparing(Quintet::getHour));

        //TODO: need to move crossover implementation to interface, continue from here

        System.out.println("in daytime crossover, after sort: ");
        System.out.println(ttSolution1);

        return null;
    }

    private void setNumOfCuttingPoints(int numOfCuttingPoints) throws Exception {
        if (numOfCuttingPoints > 0) {
            this.numOfCuttingPoints = numOfCuttingPoints;
        } else {
            throw new Exception("number of cutting points must be a positive integer");
        }
    }

    @Override
    public int getNumOfCuttingPoints() {
        return numOfCuttingPoints;
    }

    public List<Integer> getCuttingPoints() {
        return Collections.unmodifiableList(cuttingPoints);
    }

    private void setCuttingPoints() {
        Set<Integer> tempSetOfPoints = new HashSet<>();

        while (tempSetOfPoints.size() < numOfCuttingPoints) {
            //we want random points from 1 to total size of solution
            if (days * hours > 1) {
                tempSetOfPoints.add((new Random().nextInt((days * hours) - 1)) + 1);
            } else {
                cuttingPoints.add(1);
                return;
            }
        }

        for (int num : tempSetOfPoints) {
            cuttingPoints.add(num);
        }

        Collections.sort(cuttingPoints);
    }

    @Override
    public String toString() {
        return "crossover: " + this.getClass().getSimpleName() +
                "numOfCuttingPoints=" + numOfCuttingPoints;
    }
}
