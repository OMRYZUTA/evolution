package il.ac.mta.zuli.evolution.engine.evolutionengine.mutation;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimeTableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Random;

import static il.ac.mta.zuli.evolution.engine.utils.generateRandomNum;

public class Flipping<S extends Solution> implements Mutation<S> {
    double probability;
    int maxTuples;
    ComponentName component;
    TimeTable timeTable;

    public Flipping(double probability, int maxTuples, ComponentName component, TimeTable timeTable) throws Exception {
        setProbability(probability);
        this.maxTuples = maxTuples;
        this.component = component;
        this.timeTable = timeTable;
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
        List<Quintet> solutionQuintets = timeTableSolution.getSolutionQuintets();

        int numOfQuintetsToMutate = new Random().nextInt(maxTuples);


        for (int i = 0; i < numOfQuintetsToMutate; i++) {
            int randomIndexToMutate = new Random().nextInt(solutionQuintets.size());
            mutateComponent(solutionQuintets.get(randomIndexToMutate));
        }

        return (S) new TimeTableSolution(solutionQuintets);
    }

    private void mutateComponent(Quintet quintet) {
        switch (component) {
            case D:
                quintet.setDay(generateRandomDay());
                break;
            case H:
                quintet.setHour(new Random().nextInt(timeTable.getHours()));
                break;
            case C:
                int randomClassID = generateRandomNum(1, timeTable.getSchoolClasses().size());
                quintet.setSchoolClass(timeTable.getSchoolClasses().get(randomClassID));
                break;
            case T:
                int randomTeacherID = generateRandomNum(1, timeTable.getTeachers().size());
                quintet.setTeacher(timeTable.getTeachers().get(randomTeacherID));
                break;
            case S:
                int subjectID = generateRandomNum(1, timeTable.getSubjects().size());
                quintet.setSubject(timeTable.getSubjects().get(subjectID));
                break;
        }
    }

    private DayOfWeek generateRandomDay() {
        DayOfWeek[] enumValues = DayOfWeek.values();
        int randIndex = new Random().nextInt(timeTable.getDays());
        return enumValues[randIndex];
    }

    private void setProbability(double probability) throws Exception {
        if (0 <= probability && probability <= 1) {
            this.probability = probability;
        } else {
            throw new Exception("invalid probability");
        }
    }

    public int getMaxTupples() {
        return maxTuples;
    }

    public ComponentName getComponent() {
        return component;
    }

    @Override
    public String toString() {
        return "Mutation: " + this.getClass().getSimpleName() +
                "probability=" + probability +
                ", maxTupples=" + maxTuples +
                ", component=" + component;
    }

    @Override
    public String getConfiguration() {
        return String.format("max tuples = %d, Component = %s", maxTuples, component.toString());
    }

    @Override
    public double getProbability() {
        return probability;
    }
}
