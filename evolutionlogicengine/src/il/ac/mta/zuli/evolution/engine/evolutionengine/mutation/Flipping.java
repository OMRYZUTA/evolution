package il.ac.mta.zuli.evolution.engine.evolutionengine.mutation;

import il.ac.mta.zuli.evolution.engine.Quintet;
import il.ac.mta.zuli.evolution.engine.TimetableSolution;
import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.exceptions.ValidationException;
import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;
import il.ac.mta.zuli.evolution.engine.timetable.Subject;
import il.ac.mta.zuli.evolution.engine.timetable.Teacher;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

import java.time.DayOfWeek;
import java.util.*;

import static il.ac.mta.zuli.evolution.engine.EngineUtils.generateRandomNum;

public class Flipping<S extends Solution> implements Mutation<S> {
    double probability;
    int maxTuples;
    ComponentName component;
    TimeTable timeTable;

    public Flipping(double probability, int maxTuples, ComponentName component, TimeTable timeTable) {
        this.timeTable = timeTable;
        this.component = component;
        setProbability(probability);
        setMaxTuples(maxTuples);
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

        if (timeTableSolution.getSolutionSize() == 0) {
            return solution;
        }

        List<Quintet> solutionQuintets = new ArrayList<>(timeTableSolution.getSolutionQuintets());
        Set<Quintet> quintetSet = new HashSet<>(solutionQuintets);
        Quintet tempQuintet;
        int randomIndexToMutate;
        int numOfQuintetsToMutate = generateRandomNum(1, maxTuples + 1);

        for (int i = 0; i < numOfQuintetsToMutate; i++) {
            if (solutionQuintets.size() >= 1) {
                randomIndexToMutate = new Random().nextInt(solutionQuintets.size());
            } else {
                break;
            }

            tempQuintet = mutateComponent(solutionQuintets.get(randomIndexToMutate));
            solutionQuintets.set(randomIndexToMutate, tempQuintet);
            quintetSet.add(tempQuintet);
        }

        return (S) new TimetableSolution(new ArrayList<>(quintetSet), timeTable);
    }

    private Quintet mutateComponent(Quintet quintet) {
        DayOfWeek day = quintet.getDay();
        int hour = quintet.getHour();
        SchoolClass schoolClass = quintet.getSchoolClass();
        Teacher teacher = quintet.getTeacher();
        Subject subject = quintet.getSubject();

        switch (component) {
            case D:
                day = generateRandomDay();
                break;
            case H:
                hour = new Random().nextInt(timeTable.getHours());
                break;
            case C:
                int randomClassID = generateRandomNum(1, timeTable.getSchoolClasses().size());
                schoolClass = timeTable.getSchoolClasses().get(randomClassID);
                break;
            case T:
                List<Integer> TeachersIDs = timeTable.getTeachersThatTeachSubject(subject.getId());
                int randomTeachersIndex = new Random().nextInt(TeachersIDs.size());
                teacher = timeTable.getTeachers().get(TeachersIDs.get(randomTeachersIndex));
                break;
            case S:
                List<Integer> classRequiredSubjectsIDs = schoolClass.getRequiredSubjectsIDs();
                int randomIndex = new Random().nextInt(classRequiredSubjectsIDs.size());
                subject = timeTable.getSubjects().get(classRequiredSubjectsIDs.get(randomIndex));
                break;
        }

        return new Quintet(day, hour, teacher, schoolClass, subject);
    }

    private DayOfWeek generateRandomDay() {
        DayOfWeek[] enumValues = DayOfWeek.values();
        int randIndex = new Random().nextInt(timeTable.getDays());

        return enumValues[randIndex];
    }

    private void setProbability(double probability) {
        if (0 <= probability && probability <= 1) {
            this.probability = probability;
        } else {
            throw new ValidationException("probability must be between 0 -1, invalid value: " + probability);
        }
    }

    public void setMaxTuples(int maxTuples) {
        if (maxTuples < 0) {
            throw new ValidationException("MaxTuples must be >=0, invalid value: " + maxTuples);
        } else {
            this.maxTuples = maxTuples;
        }
    }

    @Override
    public String getMutationType() {
        return getClass().getSimpleName();
    }

    @Override
    public double getProbability() {
        return probability;
    }

    public int getMaxTuples() {
        return maxTuples;
    }

    public ComponentName getComponent() {
        return component;
    }

    @Override
    public String getConfiguration() {
        return String.format("max tuples = %d, Component = %s", maxTuples, component.toString());
    }

    @Override
    public String toString() {
        return "Mutation: " + this.getClass().getSimpleName() +
                "probability=" + probability +
                ", maxTuples=" + maxTuples +
                ", component=" + component;
    }
}
