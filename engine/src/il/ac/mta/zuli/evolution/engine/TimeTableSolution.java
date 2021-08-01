package il.ac.mta.zuli.evolution.engine;

import il.ac.mta.zuli.evolution.engine.evolutionengine.Solution;
import il.ac.mta.zuli.evolution.engine.rules.Rule;
import il.ac.mta.zuli.evolution.engine.timetable.SchoolClass;
import il.ac.mta.zuli.evolution.engine.timetable.Subject;
import il.ac.mta.zuli.evolution.engine.timetable.Teacher;
import il.ac.mta.zuli.evolution.engine.timetable.TimeTable;

import java.time.DayOfWeek;
import java.util.*;

public class TimeTableSolution implements Solution {
    private final Collection<Quintet> solution; //should probably be HashSet
    private final int solutionSize; //number of quintets
    private final int totalFitnessScore;
    private final Map<Rule, Integer> fitnessScorePerRole;
    TimeTable timeTable;

    public TimeTableSolution(TimeTable timeTable) {
        fitnessScorePerRole = new HashMap<>();
        this.timeTable = timeTable;
        solutionSize = generateRandomNumOfQuintets(1, calculateTotalRequiredHours());
        solution = generateQuintets(solutionSize);
        totalFitnessScore = 0;
    }

    public Collection<Quintet> getSolution() {
        return Collections.unmodifiableCollection(solution);
    }

    public int getSolutionSize() {
        return solutionSize;
    }

    public int getTotalFitnessScore() {
        return totalFitnessScore;
    }

    public Map<Rule, Integer> getFitnessScorePerRole() {
        return Collections.unmodifiableMap(fitnessScorePerRole);
    }

    @Override
    public String toString() {
        return "TimeTableSolution=" + solution +
                ", solutionSize=" + solutionSize +
                ", totalFitnessScore=" + totalFitnessScore +
                ", fitnessScorePerRole=" + fitnessScorePerRole;
    }

    private int generateRandomNumOfQuintets(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    Set<Quintet> generateQuintets(int num) {
        Set<Quintet> newSet = new HashSet<>();
        //I think the collection of quintets should be a set so that we don't have duplicate quintets

        while (newSet.size() < num) {
            newSet.add(generateRandomQuintet());
        }

        return newSet;
    }

    private Quintet generateRandomQuintet() {

        DayOfWeek randomDay = generateRandomDay();
        int randomHour = new Random().nextInt(timeTable.getHours());

        //randomly generate teacher
        int randomTeacherID = new Random().nextInt(timeTable.getTeachers().size());
        Teacher randomTeacher = timeTable.getTeachers().get(randomTeacherID);

        //randomly generate class
        int randomClassID = new Random().nextInt(timeTable.getSchoolClasses().size());
        SchoolClass randomSchoolClass = timeTable.getSchoolClasses().get(randomClassID);

        //randomly generate subject
        int randomSubjectID = new Random().nextInt(timeTable.getSubjects().size());
        Subject randomSubject = timeTable.getSubjects().get(randomSubjectID);

        return new Quintet(randomDay, randomHour, randomTeacher, randomSchoolClass, randomSubject);
    }

    private DayOfWeek generateRandomDay() {
        DayOfWeek[] enumValues = DayOfWeek.values();
        int daysInWeek = enumValues.length;
        int randIndex = new Random().nextInt(daysInWeek);
        return enumValues[randIndex];
    }

    private int calculateTotalRequiredHours() {
        int totalRequiredHours = 0;
        Map<Integer, SchoolClass> classes = timeTable.getSchoolClasses();

        for (Map.Entry<Integer, SchoolClass> entry : classes.entrySet()) {
            totalRequiredHours += entry.getValue().getTotalRequiredHours();
        }

        return totalRequiredHours;
    }


}
